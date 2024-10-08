package com.paymentonboard.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentonboard.helper.JwtTokenUtil;
import com.paymentonboard.service.EmailService;
import com.paymentonboard.util.GeneralUtil;

/*@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
*/

@RestController
public class MerchantController {
	private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static final Logger log = LoggerFactory.getLogger(MerchantController.class);
	public static String uploadDirectory = "/home/KYCDOCUMENTS";

	@Autowired
	MerchantService merchantService;
	
	@Autowired
	CopyMerchantService copyMerchantService;

	@Autowired
	RmsConfigServices rmsConfigServices;

	 @Autowired
	 EmailService emailService;

	 @Autowired
	JwtTokenUtil jwtTokenUtil;
	 
	@Autowired
	GeneralUtil generalUtil;
	
    @Autowired
   	CBMerchantMappingRepo cbMerchantMappingRepo;
	 
	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/get-merchant")
	public MerchantPaginationDto getListById(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String merchantName = js.getString("name");
		String merchantId = js.getString("mid");
		String resellerId = js.has("rid")?js.getString("rid"):"";
		String fromDate = js.getString("startDate");
		String toDate = js.getString("endDate");
		int norecord = js.getInt("pageRecords");
		int pageno = js.getInt("pageNumber");
		String tradeName = js.optString("tradeName");
		String partnerType = js.optString("partnerType");
		MerchantPaginationDto merchants = null;

		try {
			merchants = merchantService.getMerchantByName(fromDate, toDate, merchantId, resellerId, merchantName, norecord, pageno,tradeName,partnerType);
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return merchants;

	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/create-merchant")
	public Object postMerchantData(@RequestBody MerchantCreationDto merchantDto) {
		Object MerchantiInfo = null;
		try {
			log.info("is::::: {}", merchantDto.getIsPANVerified());
			MerchantiInfo = merchantService.createMerchant(merchantDto);
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return MerchantiInfo;
	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/get-merchantbank")
	public List<MerchantBank> getListbyMerchantId(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String merchantId = js.getString("merchantid");
		List<MerchantBank> ListBank = null;
		//GeneralUtil generalUtil = new GeneralUtil();
		try {

			ListBank = merchantService.getMerchantBank(merchantId);
//			ListBank.forEach(f -> {
//				try {
//					f.setRodate(GeneralUtil.convertDBDateTimeToDate(f.getRodate()));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});


		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return ListBank;
	}



	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/create-merchant-bank")
	public ResponseEntity<Response> postMerchantBankData(@RequestBody MerchantBank merchantBank) {
		Response response = merchantService.createMerchantBank(merchantBank);
		log.info("Enter in update method:::::::::::::::::::::: ");
		merchantService.updateFiledStatus(merchantBank.getMerchantId(), "Account");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PutMapping("/update-merchant-bank")
	public List<MerchantBank> postMerchantBankUpdt(@RequestBody MerchantBank merchantBank) {// TODO
		List<MerchantBank> merchantBankupdtinfo = null;
		try {
			merchantBankupdtinfo = merchantService.updateMerchantbank(merchantBank);
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return merchantBankupdtinfo;
	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/update-txn-status")
	public String postMerchantBankUpdt(@Param("pid") Long pid, @RequestBody DecentroTxnStatusResponse decentroTxnStatusResponse) {
		String merchantBankupdtinfo = "";
		try {
			merchantBankupdtinfo = merchantService.updateMerchantbankTxnStatus(pid, decentroTxnStatusResponse);
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return merchantBankupdtinfo;
	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@GetMapping("/get-merchantbank-byid")
	public List<MerchantBank> getListbyPId(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		Long Pid = js.getLong("Pid");
		List<MerchantBank> BankDetails = null;
		try {

			BankDetails = merchantService.getMerchantBankPid(Pid);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return BankDetails;
	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/delete-merchantbank-byid")
	public ResponseEntity<Response> deleteMerchantBank(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		Long Pid = js.getLong("Pid");
		return new ResponseEntity<>(merchantService.deleteMerchantbank(Pid), HttpStatus.OK);
	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/upload-image")
	public List<MerchantKycDoc> uploadImage(@RequestParam("imageFile") MultipartFile imageFile, String merchantId, String docType, String docId, Optional<String> isAdditionalDoc   ) {
		StringBuilder filesname = new StringBuilder();
		String MerchantId = merchantId;
		String DocType = docType;
		String DocId = docId;
		String fileOrgname = imageFile.getOriginalFilename();
		String filesname1 = uploadDirectory + "/" + MerchantId +"/";
		fileOrgname= fileOrgname.replaceAll("\\s", "");
		String nameFileconvert = DocId +"_"+fileOrgname;
		File file = new File(filesname1);
		if (!file.exists()) {
            file.mkdirs();
        }
		List<MerchantKycDoc> merchantKycDocList = new ArrayList<>();
		MerchantKycDoc kycdto = null;

		try {
			boolean isFileValid = FileContentValidationUtility.validateFileContent(imageFile.getInputStream());
			String extension = Objects.requireNonNull(imageFile.getOriginalFilename())
					.substring(imageFile.getOriginalFilename().lastIndexOf(".") + 1);
			if (isFileValid
					&& (extension.equalsIgnoreCase("pdf") || extension.equalsIgnoreCase("png")
					|| extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
					|| extension.equalsIgnoreCase("bmp")) || (isAdditionalDoc.isPresent() && isAdditionalDoc.get().equalsIgnoreCase("true") && 
							(extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls")))) {
				Path path = Paths.get(filesname1, nameFileconvert);
				filesname.append(nameFileconvert);
				Files.write(path, imageFile.getBytes());
				String DocRef = filesname1 + nameFileconvert;
				MerchantKycDoc merchantKycDoc = new MerchantKycDoc();


				merchantKycDoc.setDocName(nameFileconvert);
				merchantKycDoc.setDocType(DocType);
				merchantKycDoc.setDocpath(DocRef);
				merchantKycDoc.setMerchantId(MerchantId);
				merchantKycDoc.setModifiedBy(MerchantId);
				long millis = System.currentTimeMillis();
				String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(millis));
				merchantKycDoc.setModifiedOn(timeStamp);
				merchantKycDoc.setUpdatedBy(MerchantId);
				merchantKycDoc.setUpdateOn(timeStamp);
				merchantKycDocList.add(merchantKycDoc);
			}
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return merchantKycDocList;

	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
    @PostMapping("/upload-KycdocsV2")
    public List<MerchantKycDoc> uploadImageV2(@RequestBody UploadDocDto uploadDoc  ) {

        String merchantId = uploadDoc.getMerchantId();

        Map<String,UploadDocDetailsDto> docTypes = new HashMap<>();

        int i = 0;
        for(UploadDocDetailsDto dto : uploadDoc.getDocType())
        {
            docTypes.put(String.valueOf(i), dto);
            i++;
        }

        List<MerchantKycDoc> kycdto = null;
        try {
            String docType = mapper.writeValueAsString(docTypes);
            kycdto = merchantService.insertKycDoc(merchantId,docType);
            log.info("Enter in update method:Kyc::::::::::::::::::::: ");
            merchantService.updateFiledStatus(merchantId, "Kyc");

        } catch (Exception e) {
			log.error("Error :: {0}", e);
        }
        return kycdto;
    }

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/upload-Kycdocs")
	public List<MerchantKycDoc> uploadImage(@RequestParam String merchantId, String docType  ) {
		String MerchantId = merchantId;
		String DocType = docType;
		List<MerchantKycDoc> kycdto = null;

		try {
			kycdto = merchantService.insertKycDoc(MerchantId,DocType);

			log.info("Enter in update method:::::::::::::::::::::: ");
			merchantService.updateFiledStatus(merchantId, "Kyc");
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return kycdto;

	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/delete-upload-image")
	public List<MerchantDocDto> deleteImage(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		int Id = js.getInt("id");
//		String docName = js.getString("docname");
		String path = js.getString("path");
		String MerchantId = js.getString("merchantId");
		List<MerchantDocDto> kycdto = null;
		MerchantKycDoc merchantKycDoc = new MerchantKycDoc();
		try {

//			merchantKycDoc.setDocName(docName);
			merchantKycDoc.setId(Id);
			merchantKycDoc.setDocpath(path);
			merchantKycDoc.setMerchantId(MerchantId);
			kycdto = merchantService.deleteuploaddoc(merchantKycDoc);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return kycdto;

	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/update-Merchant-BasicSetup")
	public List<MerchantBasicSetupDto> updByMerchantId(@RequestBody String jsonBody) {

		JSONObject js = new JSONObject(jsonBody);
		String merchantId = js.getString("merchantid");
		String merReturnUrl = js.getString("merReturnUrl");
		String isAutoRefund = js.getString("isAutoRefund");
		String hours = js.getString("hours");
		String minutes = js.getString("minutes");
		String isPushUrl = js.getString("isPushUrl");
		String pushUrl = js.getString("pushUrl");
		String settlementCycle = js.getString("settlementCycle");
		String merchantDashboardRefund = js.getString("merchantDashboardRefund");
		String mdDisableRefundCc = js.getString("mdDisableRefundCc");
		String mdDisableRefundDc = js.getString("mdDisableRefundDc");
		String mdDisableRefundNb = js.getString("mdDisableRefundNb");
		String mdDisableRefundUpi = js.getString("mdDisableRefundUpi");
		String mdDisableRefundWallet = js.getString("mdDisableRefundWallet");
		String refundApi = js.getString("refundApi");
		String refundApiDisableCc = js.getString("refundApiDisableCc");
		String refundApiDisableDc = js.getString("refundApiDisableDc");
		String refundApiDisableNb = js.getString("refundApiDisableNb");
		String refundApiDisableUpi = js.getString("refundApiDisableUpi");
		String refundApiDisableWallet = js.getString("refundApiDisableWallet");
		String integrationType = js.getString("integrationType");
		String isretryAllowed = js.getString("isretryAllowed");
		String bpsEmailNotification = js.getString("bpsEmailNotification");
		String bpsSmsNotification = js.getString("bpsSmsNotification");
		String bpsMailReminder= js.getString("ibpsMailReminder");
		String Reporting_cycle = js.getString("reporting_cycle");
		String upi_loader = js.getString("upi_loader");
		String upi_intent = js.getString("upi_intent");
		String upi_collect = js.getString("upi_collect");
		String static_QR = js.getString("static_QR");
		String dynamic_QR = js.getString("dynamic_QR");
		String pay_page_timer = js.getString("pay_page_timer");
		List<MerchantBasicSetupDto> merBasicSetup = null;
		try {

			merBasicSetup = merchantService.setMerchantBasicInfo(merchantId, isAutoRefund, hours, minutes,
					isPushUrl, pushUrl, settlementCycle, merchantDashboardRefund, mdDisableRefundCc, mdDisableRefundDc,
					mdDisableRefundNb, mdDisableRefundUpi, mdDisableRefundWallet, refundApi, refundApiDisableCc,
					refundApiDisableDc, refundApiDisableNb, refundApiDisableUpi, refundApiDisableWallet,
					integrationType, isretryAllowed, bpsEmailNotification, bpsSmsNotification,bpsMailReminder,Reporting_cycle, upi_loader,upi_intent, upi_collect, static_QR,dynamic_QR, pay_page_timer);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return merBasicSetup;

	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/getMerchant-BasicSetupbyId")
	public List<MerchantBasicSetupDto> getByMerchantId(@RequestBody String jsonBody) {

		JSONObject js = new JSONObject(jsonBody);
		String merchantId = js.getString("merchantid");

		List<MerchantBasicSetupDto> merBasicSetupDetails = null;
		try {

			merBasicSetupDetails = merchantService.getMerchantBasicInfo(merchantId);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return merBasicSetupDetails;

	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/download-uploadimage")
	@ResponseBody
	public String getuploadImage(@RequestParam String urlfile, String docname, HttpServletResponse resp) throws IOException {

//		JSONObject js = new JSONObject(jsonBody);		
//		int businesstypeId = js.getInt("urlfile");
		String urlfile1 = urlfile;
		String fileName = docname;
		Path path = Paths.get(urlfile1);
		File file = new File(urlfile1);



		try {
			if(file.exists()) {
			if (fileName.indexOf(".doc")>-1) resp.setContentType("application/msword");
		      if (fileName.indexOf(".docx")>-1) resp.setContentType("application/msword");
		      if (fileName.indexOf(".xls")>-1) resp.setContentType("application/vnd.ms-excel");
		      if (fileName.indexOf(".csv")>-1) resp.setContentType("application/vnd.ms-excel");
		      if (fileName.indexOf(".ppt")>-1) resp.setContentType("application/ppt");
		      if (fileName.indexOf(".pdf")>-1) resp.setContentType("application/pdf");
		      if (fileName.indexOf(".zip")>-1) resp.setContentType("application/zip");

			String headerskey = "Content-Disposition";
			String headervalue = "attachment; filename=\"" + path.getFileName();
			resp.setHeader("Content-Transfer-Encoding", "binary");
			resp.setHeader(headerskey, headervalue);
			BufferedOutputStream outputstream = new BufferedOutputStream(resp.getOutputStream());
			FileInputStream fil = new FileInputStream(urlfile1);
			int len;
			byte[] buf = new byte[1024];
			while((len = fil.read(buf))> 0) {
				outputstream.write(buf, 0, len);
			}
			outputstream.close();
			resp.flushBuffer();

			}
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return "Success";

	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/download-uploadfiles")
	@ResponseBody
	public Object getuploadFiles(@RequestParam String urlfile, String docname, HttpServletResponse resp) throws IOException {
//		JSONObject js = new JSONObject(jsonBody);		
//		int businesstypeId = js.getInt("urlfile");
		String urlfile1 = urlfile;
		String fileName = docname;
		Path path = Paths.get(urlfile1);
		File file = new File(urlfile1);
		byte[] fileContent = Files.readAllBytes(file.toPath());
//		byte[] encoded = Base64.encodeBase64(FileUtils.readAllBytes(file));

		JSONObject js = new JSONObject();
	js.put("Data",Base64.getEncoder().encodeToString(fileContent));
		return js.toMap();

	}


	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
//	@PostMapping("/MerchantCreationDetails")
	@PostMapping(value="/MerchantCreationDetails", produces = "application/json")
	public ArrayList MerchantCreationDetails(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String Mid = js.getString("Mid");
//		String merchantId=js.getString("merchantId");
		ArrayList MerchantDetails = null;	
		AuditUser auditUser = GeneralUtil.getLoggedInUserId();
		try {
			JSONObject js1 = new JSONObject();
			boolean isMerchantValid = merchantService.checkMerchantIdIsValid(auditUser, Mid);
			if(!isMerchantValid) {
				ArrayList<String> arrayList = new ArrayList<>();
				js1.put("Error", "Merchant is not authorized");
				arrayList.add(js1.get("Error").toString());
				return arrayList;
			}
			MerchantDetails = merchantService.GetCreationDetails(Mid);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return MerchantDetails;
	}

	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/ChangeMerchantStatus")
	public String ChangeMerchantStatus(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String Date = dateFormat.format(date);
		String Mid = js.getString("Mid");
		String Status = js.getString("Status");
		String Remark = js.getString("Remark");
		String Type= js.getString("Approvel_type");
		String Added_By= js.getString("Added_By");

		String MerchantDetails = null;
		try {
		if(Status.equalsIgnoreCase("Suspended") && Type.equalsIgnoreCase("Suspended"))
		{
			 Date= js.getString("Date");
		}


		MerchantDetails = rmsConfigServices.Merchant_Status_change(Mid,Status,Remark,Type,Added_By);
		log.info("Mid {}"+Mid+"Status {}"+Status+"Type {}"+Type);
		if(Mid!=null && !Mid.isBlank() && Status.equalsIgnoreCase("Active") && Type.isBlank()) {
			log.info("MerchantDetails::: {}", MerchantDetails);

			if(MerchantDetails!=null) {
				JSONArray jsonArray = new JSONArray(MerchantDetails);

	            for(int i=0;i<jsonArray.length();i++)
	            {
	                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
	                String MerchantId = jsonObject1.optString("MerchantId");
	                String merchantName = jsonObject1.optString("merchant_name");
	                String emailId = jsonObject1.optString("email_id");
	                String merReturnUrl = jsonObject1.optString("mer_return_url");
	                String transactionKey = jsonObject1.optString("transaction_key");
	                String password = jsonObject1.optString("password");
	                String resellerId = jsonObject1.optString("reseller_id");
	                String resellerEmailId = jsonObject1.optString("reseller_email_id");
	                String oldStatus = jsonObject1.optString("oldStatus");
	                String emailTrigger = jsonObject1.optString("emailTrigger");
	                String resellerStatus = jsonObject1.optString("reseller_status");
	                String shortname= jsonObject1.optString("shortname");

	                if(emailTrigger.equalsIgnoreCase("0") && oldStatus.equalsIgnoreCase("Pending")) {
	                 emailService.sendSimpleMessage(MerchantId, merchantName, emailId, merReturnUrl, transactionKey, password, resellerId, resellerEmailId, resellerStatus,shortname);
	                }
	            }


			}
		}

 		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return MerchantDetails;
	}


	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/getKyc-DocumentList")
	public List<BusinessTypeDto> getDocumentList(@RequestBody String jsonBody) {

		JSONObject js = new JSONObject(jsonBody);
		int businesstypeId = js.getInt("businesstypeId");
		String merchantId = js.getString("merchantId");

		List<BusinessTypeDto> bTypeDto = null;
		try {

			bTypeDto = merchantService.getKycDocumentList(businesstypeId, merchantId);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return bTypeDto;

	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/create-merchant-bulkupload")
	public ResponseEntity<ResponseMessage> bulkupload (@RequestParam("file") MultipartFile file, String createdby) {
		String message = "";
		String fileName = file.getOriginalFilename();
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		boolean isFileValid = false;
		try {
			isFileValid = FileContentValidationUtility.validateFileContent(file.getInputStream());
		} catch(IOException ioe) {
			log.error("Error while getting Inputstream {0}", ioe);
		}

		if(!isFileValid || !extension.equalsIgnoreCase("csv")) {
			message = "Uploaded file contains Malicious content or invalid file format";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
		}

	    if (BulkCsvHelper.hasCSVFormat(file)) {
	      try {
	    	  merchantService.save(file, createdby);

	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	      } catch (Exception e) {
	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	      }
	    }

	    message = "Please upload a csv file!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/Verify-CreateMerchantRecords")
	public List<BulkMerchantList> getVerifyCsvData(@RequestBody String jsonBody) {

		JSONObject js = new JSONObject(jsonBody);
		String user = js.getString("username");

		List<BulkMerchantList> bTypeDto = null;
		try {
			bTypeDto = merchantService.getVerifyCsvData(user);
		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}
		return bTypeDto;
	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/create-merchantmdr-bulkupload")
	public List<BulkMdrList> createMerchantMdrBulkCsv(@RequestParam("file") MultipartFile file) {
		JSONObject js1 = new JSONObject();
		Map<String, Object> msgFile;
		List<BulkMdrList> listofMDR = null;
	    if (MdrBulkCsvHelper.hasCSVFormat(file)) {
	      try {
			  boolean isFileValid = FileContentValidationUtility.validateFileContent(file.getInputStream());
			  String extension = Objects.requireNonNull(file.getOriginalFilename())
					  .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			  if(isFileValid && extension.equalsIgnoreCase("csv")) {
				  msgFile = merchantService.saveMDR(file);

				  log.info("Status MDR Bulk File upload :::success {}", msgFile);

				  listofMDR = merchantService.createMerchantMdrBulkCsv();

				  log.info("Status MDR Creation :::success {}", listofMDR);
				  js1.put("Message Upload file", msgFile);
			  } else {
				  log.error("Uploaded file contains Malicious content");
			  }
	      } catch (Exception e) {
	        log.error("Status MDR Bulk File upload exception:: {0}", e);
	      }
	    }

		return listofMDR;

	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/CheckAccountBank")
	public   Map<String, Object> CheckMerchantBank (@RequestBody String Fields) {
      JSONObject js = new JSONObject(Fields);
	    	 Map<String, Object> Response=merchantService.CheckMerchantBank(js.getString("Product_Id"),js.getString("Account"));
	    return Response;
	}


	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/GetRemarks")
	public   ArrayList GetRemarks (@RequestBody String RequestParam) throws Exception {
      JSONObject js = new JSONObject(RequestParam);
      String iType= js.getString("Type");
      String Otype = js.getString("ModuleType");
      String iMerchantId = js.getString("merchant_id");
      String AppType = js.getString("AppType");
	    	ArrayList Response=merchantService.getRemarks(iType,Otype,iMerchantId,AppType);


	    	return Response;
	}

	@PostMapping("/GetInstrumentActivation")
	public ResponseEntity<Response> instrumentActivation(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		return new ResponseEntity<>(merchantService.setInstrumentType(js), HttpStatus.OK);
	}

	@PostMapping("/GetInstrumentList")
	public ResponseEntity<Response> getInstrumrntList(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String merchantId = js.getString("MerchantId");
		return new ResponseEntity<>(merchantService.getInstrumentList(merchantId), HttpStatus.OK);
	}

	@PostMapping("/DeleteGetInstrument")
	public ResponseEntity<Response> deleteInstrument(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String id = js.getString("Id");
		return new ResponseEntity<>(merchantService.deleteInstrument(id), HttpStatus.OK);
	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/upload-Otherdocs")
	public List<MerchantKycDoc> uploadOtherDocs(@RequestParam String merchantId, String docType  ) {


		String MerchantId = merchantId;
		String DocType = docType;
		List<MerchantKycDoc> kycdto = null;

		try {

			MerchantKycDoc merchantKycDoc = new MerchantKycDoc();
			kycdto = merchantService.insertKycOtherDoc(MerchantId,DocType);

		} catch (Exception e) {
			log.error("Error :: {0}", e);
		}

		return kycdto;

	}

	@PostMapping("/forgetPassword")
	public ResponseEntity<?> forgetPassword(@RequestBody String jsonBody, @RequestHeader("x-request-src") String authString) {
		JSONObject js = new JSONObject(jsonBody);
		String ValiduserId = js.getString("userId");
		Map<String, Object> responseMsg = null;
		try {
			log.info("Forget Password userId :::::----------------------------------- " + ValiduserId);
			log.info("Forget Password Header :::::----------------------------------- " + authString);
		 responseMsg= merchantService.forgetPasswordService(ValiduserId, authString);
		}catch(Exception e) {
			log.error("Error in Forget Password :::::----------------------------------- {0}", e);
		}
		return ResponseEntity.ok(responseMsg);

	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody String jsonBody) throws Exception {
		JSONObject js = new JSONObject(jsonBody);
		String tokenValidation = js.getString("token");
		String passwordChange = js.getString("password");
		if(!ValidationUtility.checkPasswordValidation(passwordChange)) {
        	log.info("Password does not match required pattern");
        	return new ResponseEntity<>(
        	          "Password does not match required pattern", 
        	          HttpStatus.BAD_REQUEST);
        }
		
		Map<String, Object> responseMsg= merchantService.resetPasswordService(tokenValidation, passwordChange);
		JSONObject js1 = new JSONObject();
		return ResponseEntity.ok(responseMsg);

	}

	@PostMapping("/reset-password-internal")
	public ResponseEntity<?> resetPasswordInternal(@RequestBody String jsonBody, @RequestHeader String Authorization) throws Exception {
		JSONObject js = new JSONObject(jsonBody);
		String oldpassword = js.getString("oldpassword");
		String newpassword = js.getString("newpassword");

		JSONObject jsonResponse = new JSONObject();

		if (!ValidationUtility.checkPasswordValidation(newpassword)) {
			log.error("Password does not match the required pattern");
			jsonResponse.put("Status", "Error");
			jsonResponse.put("Message", "Password does not match the required pattern");
			return ResponseEntity.ok(jsonResponse.toMap());
		}

		if (oldpassword.equals(newpassword)) {
			log.error("New Password should not be identical to current password");
			jsonResponse.put("Status", "Error");
			jsonResponse.put("Message", "New Password should not be identical to current password");
			return ResponseEntity.ok(jsonResponse.toMap());
		}

		String JwtToken = null;
		JwtToken = Authorization.substring(7);
		String userId = jwtTokenUtil.getUsernameFromToken(JwtToken);

		if (merchantService.checkHistoryOfPassword(newpassword, userId)) {

			log.info("New Password can not be same as your last five password");
			jsonResponse.put("Status", "Error");
			jsonResponse.put("Message", "New Password can not be same as your last five password");
			return ResponseEntity.ok(jsonResponse.toMap());
		}

		Map<String, Object> responseMsg = merchantService.resetPasswordServiceInternal(oldpassword, newpassword, userId);
		return ResponseEntity.ok(responseMsg);

	}

	@CrossOrigin(origins = {"http://localhost:4200","https://pa-preprod.1pay.in"})
	@PostMapping("/uploadLogo")
	public Map<String, Object> uploadFile(@RequestParam("uploadLogo") MultipartFile uploadLogo, String addedBy, String merchantId) {
		JSONObject js1 = new JSONObject();

		try {
			Map<String, Object> resultData = merchantService.uploadFileStatus(uploadLogo, addedBy, merchantId);

			js1.put("Data", resultData);

			log.info("js1.toMap()::::::::"+js1.toMap());

	} catch (Exception e) {
			log.error("Error :: {0}", e);
	}
		return js1.toMap();
	}

	@GetMapping("/get-merchant-names")
	public List<AdminReportResponse> getMerchantNamesByReseller(@RequestParam("resellerId") Optional<String> resellerId,@RequestParam("merchantId") Optional<String> merchantId) {
		return merchantService.getMerchantNamesByReseller(resellerId,merchantId);
	}

	@PostMapping("/upload-partner-document")
	public Response uploadPartnerDocument(@RequestParam("imageFile") MultipartFile imageFile, String partnerId, String name
			, String documentName, String merchantId, String partnerDocMstId, String partnerDocumentId) {

		BusinessPartnerDocumentDto businessPartnerDocumentDto = new BusinessPartnerDocumentDto();
		businessPartnerDocumentDto.setPartnerId(partnerId);
		businessPartnerDocumentDto.setName(name);
		businessPartnerDocumentDto.setDocumentName(documentName);
		businessPartnerDocumentDto.setMerchantId(merchantId);
		businessPartnerDocumentDto.setPartnerDocumentId(partnerDocumentId);
		businessPartnerDocumentDto.setPartnerDocMstId(partnerDocMstId);

		String fileOrgname = imageFile.getOriginalFilename();
		fileOrgname = fileOrgname.replaceAll("\\s", "");
		String filename = uploadDirectory + "/" + businessPartnerDocumentDto.getMerchantId() + "/";
		String nameFileconvert = businessPartnerDocumentDto.getPartnerDocMstId() + "_" + fileOrgname;
		File file = new File(filename);
		if (!file.exists()) {
			file.mkdirs();
		}

		try {
			boolean isFileValid = FileContentValidationUtility.validateFileContent(imageFile.getInputStream());
			String extension = Objects.requireNonNull(imageFile.getOriginalFilename())
					.substring(imageFile.getOriginalFilename().lastIndexOf(".") + 1);
			if (isFileValid
					&& (extension.equalsIgnoreCase("pdf") || extension.equalsIgnoreCase("png")
					|| extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
					|| extension.equalsIgnoreCase("bmp"))) {
				return merchantService.uploadPartnerDocument(filename, nameFileconvert, imageFile, businessPartnerDocumentDto);
			}
		} catch (Exception e) {
			log.error("Error :: {0}", e);
			return new Response(false, "Error while validating uploaded document");
		}

		return new Response(false, "Error while uploading document");

	}

	@GetMapping("/get-partner-document")
	public Response getPartnerDocuments(@RequestParam("merchantId") String merchantId, @RequestParam("businessTypeId") Integer businessTypeId) {
		return new Response(true, merchantService.getPartnerDocuments(merchantId, businessTypeId));
	}

	@DeleteMapping("/delete-partner-document")
	public Response deletePartnerDocuments(@RequestParam("partnerDocumentId") Long partnerDocumentId) {
		return merchantService.deletePartnerDocumentsById(partnerDocumentId);
	}

	@GetMapping("/get-merchant-list")
	public Response getMerchantListForSubMerchant(@RequestParam("merchantType") MerchantType merchantType,
												  @RequestParam("resellerId") Optional<String> resellerId) {
		return new Response(true, merchantService.getMerchantListForSubMerchant(merchantType, resellerId));
	}

	@PostMapping("/get-partner-document-master")
	public Response getPartnerDocumentList(@RequestBody PartnerDocumentListRequest partnerDocumentListRequest) {
		return new Response(true, merchantService.getPartnerDocumentList(partnerDocumentListRequest));
	}

	@DeleteMapping("/delete-partner")
	public Response deletePartner(@RequestParam("partnerId") Long partnerId) {
		return merchantService.deletePartnerById(partnerId);
	}

	@PostMapping("/merchant-status-change-request")
	public Response raiseStatusChangeRequests(@RequestBody StatusChangeRequestDto statusChangeRequestDto) {
		return merchantService.raiseStatusChangeRequests(statusChangeRequestDto);
	}

	@PostMapping("/merchant-status-update")
	public Response updateStatusChangeRequests(@RequestBody StatusChangeRequestDto statusChangeRequestDto) {
		return merchantService.updateStatusChangeRequests(statusChangeRequestDto);
	}

	@PostMapping("/create-merchant-group")
	public Response createMerchantGroup(@RequestBody GroupDataDto groupDataDto) {
		return merchantService.createMerchantGroup(groupDataDto);
	}

	@DeleteMapping("/delete-merchant-group")
	public Response deleteMerchantGroup(@RequestParam("groupId") Long groupId) {
		return merchantService.deleteMerchantGroup(groupId);
	}

	@GetMapping("/group-master")
	public Response getAllGroupData() {
		return merchantService.getAllGroupData();
	}

	@GetMapping("/get-group-merchants")
	public Response getMerchantsOfGroup(@RequestParam("groupId") Long groupId) {
		return merchantService.getMerchantsOfGroup(groupId);
	}
	
	@GetMapping("/merchant-user")
	public Response getAllMerchantUser(@RequestParam("merchantId") String merchantId) {
		return merchantService.getAllMerchantUser(merchantId);
	}
	
	@PostMapping("/create-user")
	public Response createUser(@RequestBody UserDto userDto) {
		return merchantService.createUser(userDto);
	}
	
	@GetMapping("/users-mapping-status")
	public Response getUsersWithMappingStatus(@RequestParam("merchantId") String merchantId,@RequestParam("userId") String userId) {
		return merchantService.getUsersWithMappingStatus(merchantId,userId);
	}


	@PostMapping("/update-user-mpg")
	public Response updateMpgForUser(@RequestBody List<UserDto> userDto) {
		return merchantService.updateMpgForUsers(userDto);
	}
	
	@GetMapping ("/user-merchant-list")
	public Response geUserMerchants() {
        String userName = GeneralUtil.getLoggedInUserId().getUsername();
		return merchantService.getUserMerchants(userName);
	}
	
	
	@GetMapping("/get-chargeback-file-detail")
	public Response getChargebackFileDetail(@RequestParam("cbId") Long cbId) {
		return merchantService.getChargebackFileDetail(cbId);
	}
	
	@PostMapping("/update-doc-status")
	public UpdateCBStatusResponse updateChargebackDocStatus(
			@RequestBody List<ChargebackRequestDto> chargebackRequestDto) {

		return merchantService.updateChargebackDocStatus(chargebackRequestDto);

	}
	
	@PostMapping("/upload-chargeback-file")
	public Response uploadChargebackDetailsForMerchant(@RequestParam(value = "chargebackFiles", required = false) MultipartFile[] chargebackFiles,
	        @RequestParam("merchantId") String merchantId, @RequestParam("status") String status,
	        @RequestParam(value="remark", required=false) String remark, @RequestParam("cbId") Long cbId,
	        @RequestParam(value = "documentTypes", required = false) String[] documentTypes) {

	    if (chargebackFiles != null && documentTypes != null && chargebackFiles.length != documentTypes.length) {
	        return new Response(false, "Incorrect number of files and document types provided.");
	    }
	    if (chargebackFiles != null && chargebackFiles.length > 3) {
	        return new Response(false, "Max 3 files must be uploaded.");
	    }
	    if (status.equalsIgnoreCase("rejected")) {
	        if (remark == null || remark.trim().isEmpty()) {
	            return new Response(false, "Remark is required when status is 'rejected'.");
	        }

	        if (chargebackFiles == null || chargebackFiles.length < 1) {
	            return new Response(false, "At least 1 file must be uploaded when status is 'rejected'.");
	        }
	    }

	    if (status.equalsIgnoreCase("accepted")) {
	        if (chargebackFiles == null || chargebackFiles.length == 0) {
	            log.info("No files uploaded as status is 'accepted'.");

	            ChargebackMerchantMapping merchantMapping = new ChargebackMerchantMapping();

	            merchantMapping.setMerchantRemark(remark);
	            merchantMapping.setMerchantStatus(status);
	            cbMerchantMappingRepo.updateMerchantStatus(merchantMapping.getMerchantStatus(),
	                    merchantMapping.getMerchantRemark(), cbId);

	            return new Response(true, "status updated successfully");
	        }
	    }

	    List<String> failedFiles = new ArrayList<>();
	    boolean allSuccessful = true;

	    if (chargebackFiles != null) {
	        for (int i = 0; i < chargebackFiles.length; i++) {
	            MultipartFile chargebackFile = chargebackFiles[i];
	            String documentType = documentTypes[i];

	            ChargebackRequestDto chargebackRequestDto = new ChargebackRequestDto();
	            chargebackRequestDto.setCbId(cbId);
	            chargebackRequestDto.setInvoiceNumber(generalUtil.generateInvoiceNumber());
	            chargebackRequestDto.setMerchantId(merchantId);
	            chargebackRequestDto.setStatus(status);
	            chargebackRequestDto.setRemark(remark);
	            chargebackRequestDto.setDocumentType(documentType);

	            try {
	                boolean isFileValid = FileContentValidationUtility.validateFileContent(chargebackFile.getInputStream());
	                String extension = Objects.requireNonNull(chargebackFile.getOriginalFilename())
	                        .substring(chargebackFile.getOriginalFilename().lastIndexOf(".") + 1);
	                if (isFileValid && (extension.equalsIgnoreCase("pdf") || extension.equalsIgnoreCase("png")
	                        || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
	                        || extension.equalsIgnoreCase("bmp") || extension.equalsIgnoreCase("xlsx")|| extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("csv")|| extension.equalsIgnoreCase("doc")|| extension.equalsIgnoreCase("docx"))) {

	                    chargebackRequestDto.setChargebackFileName(chargebackFile.getOriginalFilename());
	                    Response response = merchantService.uploadChargebackDetailsForMerchant(chargebackFile,
	                            chargebackRequestDto);
	                    if (!response.isSuccess()) {
	                        allSuccessful = false;
	                        failedFiles.add(chargebackFile.getOriginalFilename() + " - " + documentType);
	                        break;
	                    }
	                } else {
	                    allSuccessful = false;
	                    failedFiles.add(chargebackFile.getOriginalFilename() + " - Invalid file format for document type : "
	                            + documentType);
	                    break;
	                }
	            } catch (Exception e) {
	                log.error("Error :: {0}", e);
	                allSuccessful = false;
	                failedFiles.add(chargebackFile.getOriginalFilename() + " - Error while processing");
	                break;
	            }
	        }
	    }

	    if (!allSuccessful) {
	        String message = " Failed files: " + String.join(", ", failedFiles);
	        return new Response(false, message);
	    }

	    return new Response(true, "All chargeback documents uploaded successfully.");
	}

	@PostMapping("/update-chargeback-files")
	public Response updateChargebackFileForMerchant(@RequestParam("chargebackFiles") MultipartFile[] chargebackFiles,
			@RequestParam("merchantId") String merchantId, @RequestParam("cbDocIds") String[] cbDocIds,
			@RequestParam("documentTypes") String[] documentTypes) {
		if (chargebackFiles.length != documentTypes.length || cbDocIds.length != documentTypes.length) {
			return new Response(false, "Incorrect number of files, document types, or cbDocIds provided.");
		}
		if (chargebackFiles.length > 3) {
		    return new Response(false, "Max 3 files must be uploaded.");
		}
		List<String> failedFiles = new ArrayList<>();
		boolean allSuccessful = true;

		for (int i = 0; i < chargebackFiles.length; i++) {
			MultipartFile chargebackFile = chargebackFiles[i];
			String documentType = documentTypes[i];
			Long cbDocId = Long.valueOf(cbDocIds[i]);
			ChargebackRequestDto chargebackRequestDto = new ChargebackRequestDto();
			chargebackRequestDto.setCbDocId(cbDocId);
			chargebackRequestDto.setInvoiceNumber(generalUtil.generateInvoiceNumber());
			chargebackRequestDto.setMerchantId(merchantId);
			chargebackRequestDto.setDocumentType(documentType);

			try {
				boolean isFileValid = FileContentValidationUtility.validateFileContent(chargebackFile.getInputStream());
				String extension = Objects.requireNonNull(chargebackFile.getOriginalFilename())
						.substring(chargebackFile.getOriginalFilename().lastIndexOf(".") + 1);
				if (isFileValid && (extension.equalsIgnoreCase("pdf") || extension.equalsIgnoreCase("png")
						|| extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
						|| extension.equalsIgnoreCase("bmp") || extension.equalsIgnoreCase("xlsx")|| extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("csv")|| extension.equalsIgnoreCase("doc")|| extension.equalsIgnoreCase("docx"))) {

					chargebackRequestDto.setChargebackFileName(chargebackFile.getOriginalFilename());
					Response response = merchantService.uploadUpdatedChargebackDetailsForMerchant(chargebackFile,
							chargebackRequestDto);
					if (!response.isSuccess()) {
						allSuccessful = false;
						failedFiles.add(chargebackFile.getOriginalFilename() + " & document type : " + documentType
								+ " & error : " + response.getMessage());
						break;
					}
				} else {
					allSuccessful = false;
					failedFiles.add(chargebackFile.getOriginalFilename() + " - Invalid file format for document type : "
							+ documentType);
					break;
				}
			} catch (Exception e) {
				log.error("Error :: {0}", e);
				allSuccessful = false;
				failedFiles.add(chargebackFile.getOriginalFilename() + " - Error while processing");
				break;
			}
		}

		if (!allSuccessful) {
			String message = "Failed files : " + String.join(", ", failedFiles);
			return new Response(false, message);
		}

		return new Response(true, "All chargeback documents uploaded successfully.");
	}
	
	@PostMapping("/get-merchant-docs")
	public Response getMerchantUploadedDocument (@RequestParam("merchantId") String merchantId) {
      
	    return merchantService.getMerchantUploadedDocument(merchantId);
	}

	@PostMapping("/copy-merchant")
	public Response copyMerchant(@RequestParam("merchantId") String merchantId) {
		return copyMerchantService.copyMerchant(merchantId);
	}
	
}
