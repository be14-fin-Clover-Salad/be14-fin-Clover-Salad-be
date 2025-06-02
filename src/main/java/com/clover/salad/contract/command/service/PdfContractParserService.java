package com.clover.salad.contract.command.service;

import com.clover.salad.contract.command.dto.*;
import com.clover.salad.contract.document.entity.DocumentOrigin;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.regex.*;

@Slf4j
@Service
public class PdfContractParserService {

	public ContractUploadRequestDTO parsePdf(File pdfFile, DocumentOrigin documentOrigin) {
		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);

			CustomerDTO customer = new CustomerDTO();
			customer.setName(find(text, "성명\\s*([가-힣]+)"));
			customer.setBirthdate(find(text, "생년월일\\s*([\\d\\-]+)"));
			customer.setPhone(find(text, "휴대전화\\s*([\\d\\-]+)"));
			customer.setAddress(find(text, "주소\\s*(.+?)\\s*휴대전화"));
			customer.setEmail(find(text, "E-mail\\s*([\\w@.]+)"));
			customer.setCustomerType("고객");

			ContractDTO contract = new ContractDTO();
			contract.setStartDate(null); // 날짜 파싱 정규표현식 추가 필요
			contract.setEndDate(null);
			contract.setAmount(0);
			contract.setBankName(find(text, "은행명\\s*([가-힣]+)"));
			contract.setBankAccount(find(text, "계좌번호\\s*(\\d{10,})"));
			contract.setPaymentDay(25);
			contract.setDepositOwner(find(text, "예금주\\s*([가-힣]+)"));
			contract.setRelationship(find(text, "관계\\s*([가-힣]+)"));
			contract.setPaymentEmail(find(text, "결제 이메일\\s*([\\w@.]+)"));

			ContractUploadRequestDTO result = new ContractUploadRequestDTO();
			result.setCustomer(customer);
			result.setContract(contract);
			result.setDocumentOrigin(documentOrigin);

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
