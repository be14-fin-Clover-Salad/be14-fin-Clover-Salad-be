package com.clover.salad.consult.query.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import com.clover.salad.consult.query.mapper.ConsultMapper;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultQueryServiceImpl implements ConsultQueryService {

    private final ConsultMapper consultQueryMapper;
    private final JwtUtil jwtUtil;
    private final EmployeeQueryService employeeQueryService;

    private String getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_")).findFirst().orElse("ROLE_MEMBER");
    }

    private String getUserCode(String token) {
        return jwtUtil.getUsername(token);
    }

    @Override
    public List<ConsultQueryDTO> findByRole(String token) {
        String role = getUserRole();
        String code = getUserCode(token);
        LoginHeaderInfoDTO userInfo = employeeQueryService.getLoginHeaderInfo(code);

        switch (role) {
            case "ROLE_ADMIN":
                return consultQueryMapper.findAllActive();
            case "ROLE_MANAGER":
                return consultQueryMapper.findByDepartmentName(userInfo.getDepartmentName());
            default:
                return consultQueryMapper.findByEmployeeName(userInfo.getName());
        }
    }

    @Override
    public List<ConsultQueryDTO> findAll(String token) {
        return consultQueryMapper.findAll();
    }
}
