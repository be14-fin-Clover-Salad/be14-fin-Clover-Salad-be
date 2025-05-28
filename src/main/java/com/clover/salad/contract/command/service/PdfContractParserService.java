package com.clover.salad.contract.command.service;

import com.clover.salad.contract.command.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.regex.*;

@Slf4j
@Service
public class PdfContractParserService {

	public ContractUploadRequestDTO parsePdf(File pdfFile) {
		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);

			CustomerDTO customer = new CustomerDTO();
			customer.setName(find(text, "성명\\s*([가-힣]+)"));
			customer.setBirthdate(find(text, "생년월일\\s*([\\d\\-]+)"));
			customer.setGender(text.contains("□ 남") ? "남" : "여");
			customer.setPhone(find(text, "휴대전화\\s*([\\d\\-]+)"));
			customer.setAddress(find(text, "주소\\s*(.+?)\\s*휴대전화"));
			customer.setEmail(find(text, "E-mail\\s*([\\w@.]+)"));

			ContractDTO contract = new ContractDTO();
			contract.setContractNumber(find(text, "계약번호\\s*([\\w\\-]+)"));
			contract.setProductName(find(text, "렌탈제품\\s*(\\S+)"));
			contract.setMonthlyFee(find(text, "월 렌탈료\\s*(\\S+)"));
			contract.setTotalFee(find(text, "총 렌탈료\\s*(\\S+)"));
			contract.setRentalPeriod(find(text, "렌탈기간\\s*(\\S+)"));
			contract.setPaymentMethod(find(text, "납부방법\\s*(□ 카드결제|□ 자동이체)"));

			ContractUploadRequestDTO result = new ContractUploadRequestDTO();
			result.setCustomer(customer);
			result.setContract(contract);
			return result;

		} catch (Exception e) {
			throw new RuntimeException("PDF 분석 오류: " + e.getMessage(), e);
		}
	}

	private String find(String text, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);
		return matcher.find() ? matcher.group(1).trim() : "";
	}
}
