package com.clover.salad.consult.query.service;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import com.clover.salad.consult.query.mapper.ConsultMapper;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ConsultQueryServiceImpl implements ConsultQueryService {

    private final ConsultMapper consultMapper;
    private final JwtUtil jwtUtil;
    private final EmployeeQueryService employeeQueryService;

    private String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(r -> r.startsWith("ROLE_")).findFirst().orElse("ROLE_MEMBER");
    }

    private LoginHeaderInfoDTO getUserInfo(String token) {
        String code = jwtUtil.getUsername(token);
        return employeeQueryService.getLoginHeaderInfo(code);
    }

    private void assertAdmin() {
        if (!"ROLE_ADMIN".equals(getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }

    private void assertAdminOrManager() {
        String role = getRole();
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MANAGER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 데이터에 접근할 수 있는 권한이 없습니다.");
        }
    }

    @Override
    public List<ConsultQueryDTO> findAll(String token) {
        assertAdmin();
        return consultMapper.findAll();
    }

    @Override
    public ConsultQueryDTO findById(String token, int id) {
        assertAdmin();
        return consultMapper.findById(id);
    }

    @Override
    public List<ConsultQueryDTO> findAllActive(String token) {
        assertAdmin();
        return consultMapper.findAllActive();
    }

    @Override
    public ConsultQueryDTO findActiveById(String token, int id) {
        assertAdmin();
        return consultMapper.findActiveById(id);
    }

    @Override
    public List<ConsultQueryDTO> findByDepartmentName(String token) {
        assertAdminOrManager();
        LoginHeaderInfoDTO user = getUserInfo(token);
        return consultMapper.findByDepartmentName(user.getDepartmentName());
    }

    @Override
    public ConsultQueryDTO findByDepartmentNameAndId(String token, int id) {
        assertAdminOrManager();
        LoginHeaderInfoDTO user = getUserInfo(token);
        Map<String, Object> param = Map.of("departmentName", user.getDepartmentName(), "id", id);
        return consultMapper.findByDepartmentNameAndId(param);
    }

    @Override
    public List<ConsultQueryDTO> findByEmployeeCode(String token) {
        String code = jwtUtil.getUsername(token);
        return consultMapper.findByEmployeeCode(code);
    }

    @Override
    public ConsultQueryDTO findByEmployeeCodeAndId(String token, int id) {
        String code = jwtUtil.getUsername(token);
        Map<String, Object> param = Map.of("employeeCode", code, "id", id);
        return consultMapper.findByEmployeeCodeAndId(param);
    }
}
