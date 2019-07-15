package pl.pjm77.weightliftinglog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class LoginTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void loginAvailableForAll() throws Exception {
        this.mockMvc
                .perform(get("/login").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void loginValidUser() throws Exception {
        this.mockMvc
                .perform(post("/login").with(csrf())
                        .param("username", "user")
                        .param("password", "user"))
                .andExpect(authenticated());
    }

    @Test
    public void loginInvalidUser() throws Exception {
        this.mockMvc
                .perform(post("/login").with(csrf())
                        .param("username", "whoever")
                        .param("password", "whatever"))
                .andExpect(unauthenticated());
    }
}