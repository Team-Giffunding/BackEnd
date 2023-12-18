package com.example.giffunding.service;

import com.example.giffunding.config.JwtTokenProvider;
import com.example.giffunding.dto.response.KakaoLoginResponseDto;
import com.example.giffunding.dto.response.KakaoUserResponseDto;
import com.example.giffunding.dto.response.ResponseDto;
import com.example.giffunding.dto.response.TokenResponseDto;
import com.example.giffunding.entity.RefreshToken;
import com.example.giffunding.entity.User;
import com.example.giffunding.dto.request.CreateUserRequestDto;
import com.example.giffunding.repository.RefreshTokenRepository;
import com.example.giffunding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public JwtTokenProvider jwtTokenProvider;

    private final WebClient webClient;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;


    @Transactional
    public User createUser(CreateUserRequestDto requestDto) {
        User newUser = User.builder()
                .userNumber(requestDto.getUserNumber())
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .build();
        return userRepository.save(newUser);
    }

    public KakaoLoginResponseDto kakaoLogin(String authorizationCode) {
        String accessToken = getAccessToken(authorizationCode);

        User user;

        //카카오 리소스 서버에서 정보 가져오기
        Mono<KakaoUserResponseDto> userInfoMono = getUserInfo(accessToken);
        KakaoUserResponseDto userInfo = userInfoMono.block();

        Optional<User> userData = userRepository.findByUserNumber(String.valueOf(userInfo.getId()));

        if (userData.isEmpty()) {
            user = User.builder()
                    .userNumber(String.valueOf(userInfo.getId()))
                    .role("USER")
                    .email(userInfo.getEmail())
                    .nickname(userInfo.getNickname())
                    .build();

            userRepository.save(user);

            Optional<User> userLoginData = userRepository.findByUserNumber(String.valueOf(userInfo.getId()));
            String refreshToken = "Bearer " + jwtTokenProvider.createRereshToken(userLoginData.get().getId());
            KakaoLoginResponseDto tokenResponseDto = KakaoLoginResponseDto.builder()
                    .message("OK")
                    .code(200)
                    .accessToken("Bearer " + jwtTokenProvider.createAccessToken(
                            userLoginData.get().getId(),
                            String.valueOf(userLoginData.get().getRole())))
                    .refreshToken(refreshToken)
                    .userId(userLoginData.get().getId())
                    .email(userLoginData.get().getEmail())
                    .nickname(userLoginData.get().getNickname())
                    .existed(0)
                    .build();

            saveOrUpdateRefreshToken(userLoginData.get().getId(), refreshToken);

            return tokenResponseDto;
        } else {
            Optional<User> userLoginData = userRepository.findByUserNumber(String.valueOf(userInfo.getId()));
            String refreshToken = "Bearer " +jwtTokenProvider.createRereshToken(userLoginData.get().getId());
            KakaoLoginResponseDto tokenResponseDto = KakaoLoginResponseDto.builder()
                    .message("OK")
                    .code(200)
                    .accessToken("Bearer " +jwtTokenProvider.createAccessToken(
                            userLoginData.get().getId(),
                            String.valueOf(userLoginData.get().getRole())))
                    .refreshToken(refreshToken)
                    .userId(userLoginData.get().getId())
                    .email(userLoginData.get().getEmail())
                    .nickname(userLoginData.get().getNickname())
                    .existed(1)
                    .build();

            saveOrUpdateRefreshToken(userLoginData.get().getId(), refreshToken);

            return tokenResponseDto;
        }
    }

    private void saveOrUpdateRefreshToken(Long userId, String refreshToken) {
        RefreshToken existingToken = refreshTokenRepository.findByUserId(userId).orElse(new RefreshToken());
        existingToken.setUserId(userId);
        existingToken.setToken(refreshToken);
        refreshTokenRepository.save(existingToken);
    }

    public ResponseDto logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);

        return ResponseDto.builder()
                .message("OK")
                .code(200)
                .build();
    }

    public TokenResponseDto reissueToken(String refreshToken, Long userId) {
        TokenResponseDto reissueTokenResponse;

        if(!jwtTokenProvider.validateRefreshToken(refreshToken)){

            reissueTokenResponse = TokenResponseDto.builder()
                    .code(417)
                    .message("재로그인하시오")
                    .build();

            return reissueTokenResponse;
        }

        String existedRefreshToken = refreshTokenRepository.findByUserId(userId).get().getToken();

        if(existedRefreshToken.equals(refreshToken)){

            String userRole = String.valueOf(userRepository.findUserRole(userId));

            reissueTokenResponse= TokenResponseDto
                    .builder()
                    .code(200)
                    .message("OK")
                    .accessToken(jwtTokenProvider.createAccessToken(userId,userRole))
                    .refreshToken(refreshToken)
                    .build();

            return reissueTokenResponse;

        }

        reissueTokenResponse = TokenResponseDto.builder()
                .code(403)
                .message("접근이 올바르지 않습니다.")
                .build();

        return reissueTokenResponse;

    }

    private String getAccessToken(String authorizationCode) {
        return webClient.post()
                .uri(tokenUri)
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + authorizationCode +
                        "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .block();
    }

    public Mono<KakaoUserResponseDto> getUserInfo(String accessToken) {
        return webClient
                .get()
                .uri("https://kapi.kakao.com/v2/user/me") // 카카오 사용자 정보 엔드포인트
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponseDto.class);
    }
}
