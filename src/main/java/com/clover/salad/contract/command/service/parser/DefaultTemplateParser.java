package com.clover.salad.contract.command.service.parser;

import com.clover.salad.contract.command.dto.*;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

@Slf4j
@Component
public class DefaultTemplateParser implements PdfContractParsingStrategy {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ContractUploadRequestDTO parseAll(String fullText, String unused1, String unused2,
			DocumentOrigin origin) {
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
				.email(email).customerType(CustomerType.고객).build();
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

		/* 총합 계약서에 렌트 비용은 pdf에 있는 xx,xxx 원/월중 가장 큰 금액으로 계산 */
		String amount = extractLargestRentalFee(text);
		String bank = extract(text, "은행 명\\s*([가-힣a-zA-Z]+)");
		String acc = extract(text, "계좌 번호\\s*([\\d\\-]+)").replaceAll("-", "");
		String owner = extract(text, "예금주\\s*([가-힣a-zA-Z]+)");
		String rel = extract(text, "본인과의 관계\\s*([가-힣a-zA-Z]+)");
		String email = extract(text, "결제 정보 이메일\\s*([\\w@.]+)");
		String day = extract(text, "매월 납일일자\\s*(\\d{1,2})");

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
		Pattern pattern =
				Pattern.compile("(\\S+)\\s+(\\d+)\\s+(SN\\d{8})\\s+(\\S+)\\s+(\\d+)\\s*원/월");
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			String productName = matcher.group(1);
			int quantity = Integer.parseInt(matcher.group(2));
			String modelName = matcher.group(3);
			String manufacturer = matcher.group(4);

			list.add(ProductDTO.builder().productName(productName).quantity(quantity)
					.modelName(modelName).manufacturer(manufacturer).build());
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

	private String extractLargestRentalFee(String text) {
		Pattern pattern = Pattern.compile("(\\d{4,})\\s*원/월");
		Matcher matcher = pattern.matcher(text);

		int max = 0;
		while (matcher.find()) {
			int value = Integer.parseInt(matcher.group(1));
			if (value > max) {
				max = value;
			}
		}

		if (max == 0) {
			log.warn("extractLargestRentalFee 실패 - 금액 정보 없음");
		}
		return String.valueOf(max);
	}
}
