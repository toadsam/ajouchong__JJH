package com.ajouchong.controller.user;

//@RequiredArgsConstructor
//@RestController
//@RequestMapping("api/auth")
//public class MemberUserController {
//
//    private final MemberService memberService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @PostMapping("/signup")
//    public ApiResponse<Member> signup(@RequestBody AddMemberRequestDto requestDto) {
//        try {
//            Member member = memberService.save(requestDto);
//            return new ApiResponse<>(1, "회원가입에 성공했습니다.", member);
//        } catch (DuplicateEmailException e) {
//            return new ApiResponse<>(0, "이미 존재하는 이메일입니다.", null);
//        }
//    }
//
//    @PostMapping("/login")
//    public ApiResponse<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getEmail(),
//                            loginRequest.getPassword()
//                    )
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = jwtTokenProvider.generateToken(authentication.getName());
//
//            JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse(jwt);
//
//            return new ApiResponse<>(1, "로그인에 성공했습니다.", jwtResponse);
//        } catch (BadCredentialsException e) {
//            return new ApiResponse<>(0, "로그인 실패: 이메일 또는 비밀번호가 올바르지 않습니다.", null);
//        }
//
//    }
//
//    @PostMapping("/logout")
//    public ApiResponse<String> logout(HttpServletRequest request) {
//        return new ApiResponse<>(1, "로그아웃에 성공했습니다.", null);
//    }
//
//    @GetMapping("/profile")
//    public ApiResponse<Member> getMyProfile(HttpServletRequest request) {
//        String token = jwtTokenProvider.resolveToken(request);
//        System.out.println("token: " + token);
//
//        if (token == null) {
//            System.out.println("No token found in request.");
//            return new ApiResponse<>(0, "토큰이 없습니다.", null);
//        }
//
//        if (!jwtTokenProvider.validateToken(token)) {
//            System.out.println("Invalid token: " + token);
//            return new ApiResponse<>(0, "유효하지 않은 토큰입니다.", null);
//        }
//
//        String email = jwtTokenProvider.getUserEmailFromToken(token);
//        System.out.println(email);
//
//        try {
//            Member member = memberService.findByEmail(email);
//            return new ApiResponse<>(1, "회원 정보를 성공적으로 조회했습니다.", member);
//        } catch (UsernameNotFoundException e) {
//            System.out.println("Error retrieving member information: " + e.getMessage());
//            return new ApiResponse<>(0, "회원 정보를 찾을 수 없습니다.", null);
//        } catch (Exception e) {
//            System.out.println("Error retrieving member information: " + e.getMessage());
//            return new ApiResponse<>(0, "회원 정보 조회 중 오류가 발생했습니다.", null);
//        }
//    }
//
//    @PatchMapping("/changePw")
//    public ApiResponse<String> changePassword(@RequestHeader("Authorization") String token,
//                                              @RequestBody ChangePasswordRequestDto requestDto) {
//
//        String jwt = token.substring(7);
//        String email = jwtTokenProvider.getUserEmailFromToken(jwt);
//
//        Member member = memberService.findByEmail(email);
//        memberService.changePassword(member, requestDto.getOldPassword(), requestDto.getNewPassword());
//
//        return new ApiResponse<>(1, "비밀번호 변경이 완료되었습니다.", null);
//    }
//}
