package com.ServiceImpl;

import java.io.File;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.collections.map.HashedMap;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;

import com.Repository.RoleRepo;
import com.Repository.UserRepo;
import com.Service.AuthenticationServices;
import com.Service.JwtService;
import com.Service.TokenBlacklistService;

import antlr.collections.List;
import io.netty.util.internal.ResourcesUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import com.DTO.LoginRequest;
import com.DTO.LoginResponse;
import com.DTO.RegisterRequest;
import com.DTO.UserResponseDTO;
import com.Entity.Role;
import com.Entity.User;
import com.Enum.KycStatusEnum;
import com.Enum.RolesEnum;
import com.Enum.UserStatusEnum;
import com.Exceptions.UserAlreadyExist;
import com.Exceptions.UserNotFound;
import com.Exceptions.UserRoleExist;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationServices {

	@Autowired
	private UserRepo us;

	@Autowired
	private RoleRepo rp;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder passwordEnc;
	
	@Autowired
    private TokenBlacklistService tokenBlacklistService;

	@Override
	public UserResponseDTO registerUser(RegisterRequest registerRequest) {

		log.info("Register User Details" + registerRequest);

		if (us.existsByEmail(registerRequest.getEmail())) {

			throw new UserAlreadyExist("Already exist User");
		} else {

			ModelMapper model = new ModelMapper();

			User user = model.map(registerRequest, User.class);

			Role role = rp.findByName(RolesEnum.USER).get();

//			if (us.existsByRole(user.getRole())) {
//
//				try {
//					throw new UserRoleExist("User already has role");
//				} catch (Exception e) {
//					user.setStatus(UserStatusEnum.ACTIVE);
//					user.setKycStatus(KycStatusEnum.PENDING);
//					User u = us.save(user);
//
//					ModelMapper model1 = new ModelMapper();
//
//					UserResponseDTO userResponse = model1.map(u, UserResponseDTO.class);
//
//					return userResponse;
//				}
//			}
//			Role role = new Role();
//			role.setId(1);
//			role.setName(RolesEnum.USER);
//			role.setDescription("Default user role");
			user.setPassword(passwordEnc.encode(registerRequest.getPassword()));
			user.setRole(role);
			user.setStatus(UserStatusEnum.ACTIVE);
			user.setKycStatus(KycStatusEnum.PENDING);
			User u = us.save(user);

			ModelMapper model1 = new ModelMapper();

			UserResponseDTO userResponse = model1.map(u, UserResponseDTO.class);

			return userResponse;
		}

	}

	@Override
	public LoginResponse authenticate(LoginRequest loginRequest) {
		System.out.println(loginRequest);

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		User authenticateUser = us.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException(loginRequest.getEmail()));

//		authenticateUser.setBlacklisted(false);

		Instant currentDate = Instant.now();

		User updatedUser = us.save(authenticateUser);

		String jwtToken = jwtService.generateToken(updatedUser);

		return LoginResponse.builder().userId(updatedUser.getId().toString()).token(jwtToken)
				.expiresAt(currentDate.plusMillis(jwtService.getExpirationTime()).toString()).build();
	}

	@Override
	public UserResponseDTO getAuthenticatedUser() {
		log.debug("getAuthenticatedUser()");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		log.info("User found with email: {}", currentUser.getEmail());

		ModelMapper map = new ModelMapper();
		UserResponseDTO ur = map.map(currentUser, UserResponseDTO.class);
		return ur;
	}

	@Override
	public UserResponseDTO updateUserInservice(long id, @Valid RegisterRequest registerRequest) {

		User user = us.findById(id).orElseThrow(() -> new UserNotFound("User Not Found With This ID"));
		ModelMapper model = new ModelMapper();

		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setLastName(registerRequest.getLastName());
		user.setFirstName(registerRequest.getFirstName());
		user.setMobileNumber(registerRequest.getMobileNumber());

		Role role = rp.findByName(RolesEnum.USER).get();
		user.setRole(role);
	
		User savedUser = us.save(user);

		UserResponseDTO response = model.map(savedUser, UserResponseDTO.class);
		return response;
	}

	@Override
	public boolean getKycStatus(Long userId) {
		//User user = us.findById(userId).get();
				//.orElseThrow(() -> new UserNotFound("User", "id", userId.toString()));
		User user1 = us.findById(userId).orElseThrow(() -> new UserNotFound("User Not Found With This ID"));
				return user1.getKycStatus().name().equalsIgnoreCase("FULLY_VERIFIED") ? false : true ;

	}

	@Override
	public void getReportInService(String format) {
		
//		System.out.println("in service");
//		java.util.List<User> user = us.findAll();
//		System.out.println(user);
//		
//		try {
//			
//			File file = ResourceUtils.getFile("classpath:UserInfo.jrxml");
//			
//			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//			
//			Map<String, Object> map=new HashMap<String, Object>();
//			map.put("Created by", "DJ");
//			
//			JRBeanCollectionDataSource datasource= new JRBeanCollectionDataSource(user);
//			JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, map, datasource);
//			
//			
//			if (format.equalsIgnoreCase("pdf")) {
//				JasperExportManager.exportReportToPdfFile(fillReport, "D:\\Project\\report\\UserInfo.pdf");
//			}else if (format.equalsIgnoreCase("html")) {
//				JasperExportManager.exportReportToHtmlFile(fillReport, "D:\\Project\\report\\UserInfo.html");;
//			}else if (format.equalsIgnoreCase("csv")) {
//				JRCsvExporter csvexport=new JRCsvExporter();
//				csvexport.setExporterInput(new SimpleExporterInput(fillReport));
//				csvexport.setExporterOutput(new SimpleWriterExporterOutput("D:\\Project\\report\\UserInfo.csv"));
//				
//				csvexport.exportReport();
//			}
//			
//		} catch (Exception e) {
//			log.error("jasper exception");	
//			}
//		
//		}
		log.info("Generating report in format: {}", format);

	    java.util.List<User> users = us.findAll();
	    log.debug("Fetched users: {}", users);

	    try {
	        File file = ResourceUtils.getFile("classpath:UserInfo.jrxml");
	        log.info("JRXML file loaded: {}", file.getAbsolutePath());

	        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("Created by", "DJ");

	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

	        String outputPath = "D:\\Project\\report\\UserInfo." + format.toLowerCase();

	        switch (format.toLowerCase()) {
	            case "pdf":
	                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
	                break;

	            case "html":
	                JasperExportManager.exportReportToHtmlFile(jasperPrint, outputPath);
	                break;

	            case "csv":
	                JRCsvExporter csvExporter = new JRCsvExporter();
	                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(outputPath));
	                csvExporter.exportReport();
	                break;

	            default:
	                log.warn("Unsupported format: {}", format);
	        }

	        log.info("Report generated at: {}", outputPath);

	    } catch (Exception e) {
	        log.error("JasperReports generation failed: {}", e.getMessage(), e);
	    }
	}

	@Override
	public void logout(String jwt) {
		 String email = jwtService.extractUsername(jwt);
	        tokenBlacklistService.blacklistToken(email);
	}
	
}
