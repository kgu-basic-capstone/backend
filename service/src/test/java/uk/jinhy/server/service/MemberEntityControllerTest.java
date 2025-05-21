package uk.jinhy.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import uk.jinhy.server.api.member.domain.Member;
import uk.jinhy.server.api.member.presentation.MemberDto;
import uk.jinhy.server.service.member.controller.MemberControllerImpl;
import uk.jinhy.server.service.member.domain.MemberEntity;
import uk.jinhy.server.service.member.service.MemberServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberEntityControllerTest {

    @InjectMocks
    MemberControllerImpl memberController;

    @Mock
    MemberServiceImpl memberService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("회원가입에 성공하는 경우 http 상태와 메시지, 회원 정보를 반환한다.")
    @Test
    void 회원가입_성공시_응답결과() throws Exception {
        //given
        MemberDto.MemberSaveRequestDto request = MemberDto.MemberSaveRequestDto.builder()
                .name("testMember")
                .age(20)
                .email("test@test.com")
                .build();

        when(memberService.save(any()))
                .thenReturn(Member.builder().build());

        //when & then
        mockMvc.perform(
                        post("/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }
}
