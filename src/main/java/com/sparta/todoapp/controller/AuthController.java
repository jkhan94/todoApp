//package com.sparta.todoapp.controller;
//
//import com.sparta.todoapp.entity.UserRoleEnum;
//import com.sparta.todoapp.jwt.JwtUtil;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
//@RestController
//@RequestMapping("/api")
//public class AuthController {
//    // 쿠키 이름을 상수로 선언
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//
//    // JWT
//    private final JwtUtil jwtUtil;
//
//    // @RequiredArgsConstructor 써도 됨
//    public AuthController(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    // response 헤더 > Set-Cookie::Authorization=Robbie%20Auth; Max-Age=1800; Expires=Thu, 23 May 2024 12:42:14 GMT; Path=/
//    @GetMapping("/create-cookie")
//    public String createCookie(HttpServletResponse res) {
//        addCookie("Robbie Auth", res);
//
//        return "createCookie";
//    }
//
//    // 개발자도구 > request header > Cookie:Authorization=Robbie%20Auth
//    @GetMapping("/get-cookie")
//    public String getCookie(@CookieValue(AUTHORIZATION_HEADER) String value) {
//        System.out.println("value = " + value);
//
//        return "getCookie : " + value;
//    }
//
//    public static void addCookie(String cookieValue, HttpServletResponse res) {
//        // Cookie Value 에는 공백이 불가능해서 encoding 진행
//        // 공백 -> %20 변경
//        cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
//
//        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, cookieValue); // Name-Value
//        cookie.setPath("/");
//        cookie.setMaxAge(30 * 60);
//
//        // Response 객체에 Cookie 추가
//        res.addCookie(cookie);
//    }
//
//    // -----------------------------------------------------------
//    // http://localhost:8080/api/create-session
//    //응답 헤더 > Set-Cookie: JSESSIONID=D32DFF4435AD195F903BB25530C00D92; Path=/; HttpOnly
//    @GetMapping("/create-session")
//    public String createSession(HttpServletRequest req) {
//        // 세션이 존재할 경우 세션 반환, 없을 경우 새로운 세션을 생성한 후 반환
//        HttpSession session = req.getSession(true);
//
//        // 세션에 저장될 정보 Name - Value 를 추가합니다.
//        session.setAttribute(AUTHORIZATION_HEADER, "Robbie Auth");
//
//        return "createSession";
//    }
//
//    // http://localhost:8080/api/get-session
//    //리퀘스트 헤더 : Cookie:Authorization=Robbie%20Auth; JSESSIONID=D32DFF4435AD195F903BB25530C00D92
//    @GetMapping("/get-session")
//    public String getSession(HttpServletRequest req) {
//        // 세션이 존재할 경우 세션 반환, 없을 경우 null 반환
//        // 가져오는 것이므로 true로 생성할 필요 없다.
//        HttpSession session = req.getSession(false);
//
//        String value = (String) session.getAttribute(AUTHORIZATION_HEADER); // 가져온 세션에 저장된 Value 를 Name 을 사용하여 가져옵니다.
//        System.out.println("value = " + value);
//
//        return "getSession : " + value;
//    }
//
//    // --------------------------------------------------------------
//    @GetMapping("/create-jwt")
//    public String createJwt(HttpServletResponse res) {
//        // Jwt 생성
//        String token = jwtUtil.createToken("Robbie", UserRoleEnum.USER);
//
//        // Jwt 쿠키 저장
//        jwtUtil.addJwtToCookie(token, res);
//
//        return "createJwt : " + token;
//    }
//
//    @GetMapping("/get-jwt")
//    public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
//        // JWT 토큰 substring
//        String token = jwtUtil.substringToken(tokenValue);
//
//        // 토큰 검증
//        if (!jwtUtil.validateToken(token)) {
//            throw new IllegalArgumentException("Token Error");
//        }
//
//        // 토큰에서 사용자 정보 가져오기
//        Claims info = jwtUtil.getUserInfoFromToken(token);
//        // 사용자 username
//        String username = info.getSubject();
//        System.out.println("username = " + username);
//        // 사용자 권한
//        String authority = (String) info.get(JwtUtil.AUTHORIZATION_KEY);
//        System.out.println("authority = " + authority);
//
//        return "getJwt : " + username + ", " + authority;
//    }
//}