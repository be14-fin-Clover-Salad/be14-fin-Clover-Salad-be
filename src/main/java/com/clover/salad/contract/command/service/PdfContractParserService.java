package com.clover.salad.contract.command.service;

import com.clover.salad.contract.command.dto.*;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PdfContractParserService {

	public ContractUploadRequestDTO parsePdf(File pdfFile, DocumentOrigin documentOrigin) {
		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document).trim();
			log.info("PDF 추출 텍스트:\n{}", text);

			CustomerDTO customer = new CustomerDTO();
			ContractDTO contract = new ContractDTO();
			List<ProductDTO> products = new ArrayList<>();

			// 고객 정보
			customer.setName(extract(text, "이름\\s*([가-힣]+)"));
			customer.setBirthdate(extract(text, "생년월일\\s*(\\d{4}-\\d{2}-\\d{2})"));
			customer.setAddress(extract(text, "주소\\s*([^\n]+)"));
			customer.setPhone(extract(text, "연락처\\s*([\\d\\-]+)").replaceAll("[^\\d]", ""));
			customer.setEmail(extract(text, "이메일\\s*([\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,})"));
			customer.setCustomerType("고객");

			// 계약 날짜
			Matcher dateMatcher = Pattern.compile("계약 시작일\\s*(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{4}-\\d{2}-\\d{2})").matcher(text);
			if (dateMatcher.find()) {
				contract.setStartDate(LocalDate.parse(dateMatcher.group(1)));
				contract.setEndDate(LocalDate.parse(dateMatcher.group(2)));
			} else {
				log.warn("계약 시작일/종료일 추출 실패");
			}

			// 계약 금액 (최대 3개 합산)
			Matcher amountMatcher = Pattern.compile("(\\d{4,})\\s*원/월").matcher(text);
			int total = 0, count = 0;
			while (amountMatcher.find() && count < 3) {
				total += Integer.parseInt(amountMatcher.group(1));
				count++;
			}
			contract.setAmount(total);

			// 은행명
			contract.setBankName(extract(text, "은행 명\\s*([가-힣]+)"));

			// 계좌번호: 고성연 [예금주] 뒤의 5일(납일일자) 뒤
			contract.setBankAccount(extract(text, "고성연\\s*\\d{1,2}일\\s*([\\d\\-]+)").replaceAll("[^\\d]", ""));

			// 예금주
			contract.setDepositOwner(extract(text, "원/월\\s*([가-힣]{2,})\\s*\\d{1,2}일"));

			// 납일일자
			try {
				contract.setPaymentDay(Integer.parseInt(extract(text, "고성연\\s*(\\d{1,2})일")));
			} catch (Exception e) {
				log.warn("납일일자 파싱 실패, 기본값 25 적용");
				contract.setPaymentDay(25);
			}

			// 관계
			contract.setRelationship(extract(text, "관계\\s*([가-힣]+)"));

			// 결제 이메일
			contract.setPaymentEmail(extract(text, "결제 정보 이메일\\s*([\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,})"));

			// 상품 파싱: 청소기 3, 정수기 1, 비데 1
			String[] productNames = extractAll(text, "(청소기|정수기|비데)").toArray(new String[0]);
			String[] quantities = extractAll(text, "(?<=\\n|\\s)(\\d)(?=\\n|\\s)").toArray(new String[0]);
			String[] modelNames = extractAll(text, "SN\\d+").toArray(new String[0]);
			String[] manufacturers = extractAll(text, "(삼성|LG|쿠쿠)").toArray(new String[0]);

			int productCount = Math.min(Math.min(productNames.length, quantities.length),
				Math.min(modelNames.length, manufacturers.length));

			for (int i = 0; i < productCount; i++) {
				ProductDTO product = new ProductDTO();
				product.setProductName(productNames[i]);
				product.setQuantity(Integer.parseInt(quantities[i]));
				product.setModelName(modelNames[i]);
				product.setManufacturer(manufacturers[i]);
				products.add(product);
			}

			// 조립
			ContractUploadRequestDTO result = new ContractUploadRequestDTO();
			result.setCustomer(customer);
			result.setContract(contract);
			result.setProducts(products);
			result.setDocumentOrigin(documentOrigin);

			log.info("[CustomerDTO] {}", customer);
			log.info("[ContractDTO] {}", contract);
			log.info("[ProductDTO List] {}", products);

			return result;

		} catch (Exception e) {
			throw new RuntimeException("PDF 분석 실패: " + e.getMessage(), e);
		}
	}

	private String extract(String text, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return "";
	}

	private List<String> extractAll(String text, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);
		List<String> matches = new ArrayList<>();
		while (matcher.find()) {
			matches.add(matcher.group().trim());
		}
		return matches;
	}
}
