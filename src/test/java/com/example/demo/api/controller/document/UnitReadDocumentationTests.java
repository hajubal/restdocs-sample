package com.example.demo.api.controller.document;

import com.example.demo.ApiDocumentationTest;
import com.example.demo.service.dto.UnitDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.api.controller.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.example.demo.api.controller.document.utils.ApiDocumentUtils.getDocumentResponse;
import static com.example.demo.api.controller.document.utils.DocumentFormatGenerator.getDateFormat;
import static com.example.demo.api.controller.document.utils.DocumentLinkGenerator.DocUrl.GENDER;
import static com.example.demo.api.controller.document.utils.DocumentLinkGenerator.DocUrl.JOB;
import static com.example.demo.api.controller.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.example.demo.api.controller.document.utils.DocumentLinkGenerator.generateText;
import static com.example.demo.domain.Gender.MALE;
import static com.example.demo.domain.Job.EMPLOYEE;
import static com.example.demo.domain.Job.STUDENT;
import static java.time.LocalDate.of;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UnitReadDocumentationTests extends ApiDocumentationTest {

    @Test
    public void findById() throws Exception {

        //given
        given(unitService.findById(1L))
                .willReturn(UnitDto.builder()
                        .id(1L)
                        .name("고길동")
                        .job(EMPLOYEE)
                        .gender(MALE)
                        .birthDate(of(1958, 9, 19))
                        .hobby("바둑")
                        .build()
                );

        //when
        ResultActions result = this.mockMvc.perform(
                get("/v1/units/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("units-find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업 코드"),
                                fieldWithPath("jobName").type(JsonFieldType.STRING).description("직업 명"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 코드"),
                                fieldWithPath("genderName").type(JsonFieldType.STRING).description("성별 명"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("생년월일"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("취미")
                        )
                ));
    }

    @Test
    public void findAll() throws Exception {

        //given
        List<UnitDto> responseList = Arrays.asList(
                UnitDto.builder()
                        .id(1L)
                        .name("고길동")
                        .job(EMPLOYEE)
                        .gender(MALE)
                        .birthDate(of(1958, 9, 19))
                        .hobby("바둑")
                        .build(),
                UnitDto.builder()
                        .id(2L)
                        .name("고철수")
                        .job(STUDENT)
                        .gender(MALE)
                        .birthDate(of(2010, 7, 4))
                        .hobby("컴퓨터 게임")
                        .build()
        );

        given(unitService.findAll())
                .willReturn(responseList);

        //when
        ResultActions result = this.mockMvc.perform(
                get("/v1/units")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("units-find-all",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(beneathPath("data[]").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description(generateLinkCode(JOB)),
                                fieldWithPath("jobName").type(JsonFieldType.STRING).description(generateText(JOB)),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description(generateLinkCode(GENDER)),
                                fieldWithPath("genderName").type(JsonFieldType.STRING).description(generateText(GENDER)),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("생년월일"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("취미")
                        )
                ));
    }
}
