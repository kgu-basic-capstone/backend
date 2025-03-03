package uk.jinhy.server.service.test.example.test.member;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.jinhy.server.service.test.example.src.member.controller.MemberController;
import uk.jinhy.server.service.test.example.src.member.controller.MemberSaveRequestDto;
import uk.jinhy.server.service.test.example.src.member.domain.Member;
import uk.jinhy.server.service.test.example.src.member.service.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @InjectMocks
    MemberController memberController;

    @Mock
    MemberService memberService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("회원가입에 성공하면 http 상태와 메시지, 회원 정보를 반환한다.")
    @Test
    void saveSuccess() throws Exception {
        //given
        MemberSaveRequestDto request = MemberSaveRequestDto.builder()
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