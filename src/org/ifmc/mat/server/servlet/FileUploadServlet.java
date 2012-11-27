package org.ifmc.mat.server.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.client.codelist.service.SaveUpdateCodeListResult;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.model.Code;
import org.ifmc.mat.server.exception.ExcelParsingException;
import org.ifmc.mat.server.service.CodeListNotUniqueException;
import org.ifmc.mat.server.service.CodeListOidNotUniqueException;
import org.ifmc.mat.server.service.CodeListService;
import org.ifmc.mat.server.service.InvalidLastModifiedDateException;
import org.ifmc.mat.server.service.ValueSetLastModifiedDateNotUniqueException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.oreilly.servlet.MultipartRequest;



public class FileUploadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	protected ApplicationContext context;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {   
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		FileInputStream fis = null;
		String codeListID = request.getParameter("codeListID");
		File file = null;
		try {
			MultipartRequest multi =  new MultipartRequest(request, ".", 5048576);
			file = multi.getFile("uploadFormElement");
			String fileName = file.getName();
			fis = new FileInputStream(file);
			sendFileToParser(fis,codeListID,out,fileName);
			file.delete();
		}
		catch (Exception e) {
			e.printStackTrace();
			out.println(MatContext.get().getMessageDelegate().getInvalidTemplateMessage());
		}finally {
			try {
				 if(fis != null)
					 fis.close();
				 if(file != null){
					 if( file.exists())
						 file.delete(); 
				 }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void  sendFileToParser(FileInputStream fis,String codeListID,PrintWriter out,String fileName){
		ExcelSheetParser xlParser = new ExcelSheetParser();
		HashSet<Code> setofCodes = null;
		try {
			setofCodes = xlParser.readExcel(fis,fileName);
			if(setofCodes!= null && !setofCodes.isEmpty()){
				//find the codeList for the given codeListID.
				ArrayList<Code> listOfCodes = new ArrayList<Code>(setofCodes);
				ManageCodeListDetailModel codeListModel = getCodeListService().getCodeList(codeListID);
				codeListModel.setCodes(listOfCodes);
				codeListModel.setExistingCodeList(true);
				codeListModel.setImportFlag(true);
				//Save it to the database.
				
				SaveUpdateCodeListResult result = null;
				try {
					result = getCodeListService().saveorUpdateCodeList(codeListModel);
					if(result.isSuccess() && !result.isDuplicateExists()){
						out.println( MatContext.get().getMessageDelegate().getImportSuccessMessage() );
					}else{
						if(result.isAllCodesDups())
							// error message
							out.println(MatContext.get().getMessageDelegate().getDuplicateCodesMessage());
						else
							// success message
							out.println(MatContext.get().getMessageDelegate().getImportSuccessMessage(result.getDuplicateCount()) );
						
					}
				} catch (CodeListNotUniqueException e) {
					if(result != null)
						result.setSuccess(false);
					e.printStackTrace();
				} catch (CodeListOidNotUniqueException e) {
					e.printStackTrace();
				} catch (InvalidLastModifiedDateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValueSetLastModifiedDateNotUniqueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (ExcelParsingException e1) {
			out.println(e1.getErrorMessage());
		}
		
	}

	public CodeListService getCodeListService() {
		return (CodeListService)context.getBean("codeListService");
	}

} 



