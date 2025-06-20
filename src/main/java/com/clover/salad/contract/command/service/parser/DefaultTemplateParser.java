package com.clover.salad.contract.command.service.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.clover.salad.contract.command.dto.ContractDTO;
import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.CustomerDTO;
import com.clover.salad.contract.command.dto.ProductDTO;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultTemplateParser implements PdfContractParsingStrategy {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ContractUploadRequestDTO parseAll(String fullText, String unused1, String unused2,
			DocumentOrigin origin) {
		fullText = normalizePdfText(fullText);
		CustomerDTO customer = parseCustomer(fullText);
		ContractDTO contract = parseContract(fullText);
		List<ProductDTO> products = parseProducts(fullText);

		log.info("[파싱된 고객 정보] {}", customer);
		log.info("[파싱된 계약 정보] {}", contract);
		products.forEach(p -> log.info("[파싱된 상품 정보] {}", p));

		return ContractUploadRequestDTO.builder().customer(customer).contract(contract)
				.products(products).documentOrigin(origin).build();
	}

	/*
	*
	* */
	private CustomerDTO parseCustomer(String text) {
		String name = extract(text, "이름\\s*([가-힣]+)");
		String birthdate = extract(text, "생년월일\\s*(\\d{4}-\\d{2}-\\d{2})");
		String address = extract(text, "주소\\s*([^\n]+)");
		String phone = extract(text, "연락처\\s*([\\d\\-]+)").replaceAll("-", "");
		String email = extract(text, "이메일\\s*([\\w@.]+)");

		return CustomerDTO.builder().name(name).birthdate(birthdate).address(address).phone(phone)
				.email(email).customerType(CustomerType.CUSTOMER).build();
	}

	private ContractDTO parseContract(String text) {
		String start = "", end = "";
		try {
			Pattern datePattern = Pattern.compile(
					"계약 시작일\\s*(\\d{4}-\\d{2}-\\d{2})\\s*계약 종료일\\s*(\\d{4}-\\d{2}-\\d{2})");
			Matcher dateMatcher = datePattern.matcher(text);
			if (dateMatcher.find()) {
				start = dateMatcher.group(1);
				end = dateMatcher.group(2);
			}
		} catch (Exception e) {
			log.warn("날짜 추출 실패: {}", e.getMessage());
		}


		String amount = extractTotalRentalAmount(text);
		String bank = extract(text, "은행명\\s*([가-힣a-zA-Z]+)");
		String acc = extract(text, "계좌 번호\\s*([\\d\\-]+)").replaceAll("-", "");
		String owner = extract(text, "예금주\\s*([가-힣a-zA-Z]+)");
		String rel = extract(text, "본인과의 관계\\s*([가-힣a-zA-Z]+)");
		String email = extract(text, "결제 정보 이메일\\s*([\\w@.]+)");
		String day = extract(text, "매월 납입일자\\s*(\\d{1,2})일");

		try {
			return ContractDTO.builder().startDate(LocalDate.parse(start, DATE_FORMAT))
					.endDate(LocalDate.parse(end, DATE_FORMAT)).amount(Integer.parseInt(amount))
					.bankName(bank).bankAccount(acc).depositOwner(owner).relationship(rel)
					.paymentEmail(email).paymentDay(Integer.parseInt(day)).build();
		} catch (Exception e) {
			log.warn("날짜 또는 숫자 파싱 실패: {}", e.getMessage());
			return ContractDTO.builder().startDate(null).endDate(null).amount(0).bankName(bank)
					.bankAccount(acc).depositOwner(owner).relationship(rel).paymentEmail(email)
					.paymentDay(0).build();
		}
	}

	private List<ProductDTO> parseProducts(String text) {
		List<ProductDTO> list = new ArrayList<>();
		List<String> lines = List.of(text.split("\n"));

		// 한 줄짜리 상품 정규식: 합계는 생략 가능 (마지막 숫자 하나는 optional)
		Pattern pattern = Pattern.compile(
				"([가-힣a-zA-Z0-9 ]+?)\\s+(\\d+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)(?:\\s+(\\d+))?");

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i).trim();

			// 만약 마지막 숫자(합계)가 없으면, 다음 줄을 붙여서 다시 매칭 시도
			if (!line.matches(".*\\s+\\d{5,}$") && i + 1 < lines.size()) {
				String nextLine = lines.get(i + 1).trim();
				if (nextLine.matches("^\\d{5,}$")) {
					line += " " + nextLine;
					i++; // 다음 줄은 건너뜀
				}
			}

			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String rawName = matcher.group(1).trim();
				int quantity = Integer.parseInt(matcher.group(2));
				String modelName = matcher.group(3).trim();
				String manufacturer = matcher.group(4).trim();
				// group(5): 월 렌탈료, group(6): 합계 (생략됨)

				list.add(ProductDTO.builder().productName(rawName).quantity(quantity)
						.modelName(modelName).manufacturer(manufacturer).build());
			}
		}

		if (list.isEmpty()) {
			log.warn("상품 정보 없음");
		}

		return list;
	}



	private String extract(String text, String pattern) {
		try {
			Matcher matcher = Pattern.compile(pattern).matcher(text);
			if (matcher.find()) {
				return matcher.group(1).trim();
			} else {
				log.warn("extract 실패 - 패턴: [{}]", pattern);
				return "";
			}
		} catch (Exception e) {
			log.warn("extract 예외 발생 - 패턴: [{}], 에러: {}", pattern, e.getMessage());
			return "";
		}
	}

	private String extractTotalRentalAmount(String text) {
		int startIdx = text.indexOf("[계약 조건]");
		int endIdx = text.indexOf("[납부방법]");

		if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx) {
			log.warn("extractTotalRentalAmount 실패 - 섹션 범위 지정 오류");
			return "0";
		}

		String contractSection = text.substring(startIdx, endIdx);

		// 숫자만으로 된 단어만 추출 (영문/한글과 붙어 있지 않은 순수 숫자)
		Pattern pattern = Pattern.compile("\\b\\d+\\b"); // word boundary (\b) 사용
		Matcher matcher = pattern.matcher(contractSection);

		int max = 0;
		while (matcher.find()) {
			try {
				int value = Integer.parseInt(matcher.group());
				if (value > max) {
					max = value;
				}
			} catch (NumberFormatException e) {
				log.warn("숫자 파싱 실패: {}", matcher.group());
			}
		}

		if (max == 0) {
			log.warn("extractTotalRentalAmount 실패 - 유효한 숫자 없음");
		}

		return String.valueOf(max);
	}

	private String normalizePdfText(String text) {
		return text.replaceAll("\\u0000", " ") // NUL 제거
				.replaceAll("[ ]{2,}", " ") // 연속 공백 정리
				.trim();
	}

}
