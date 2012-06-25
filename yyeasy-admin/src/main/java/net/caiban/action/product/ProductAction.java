/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-7
 */
package net.caiban.action.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.caiban.action.BaseAction;
import net.caiban.action.util.CacheConstFacade;
import net.caiban.domain.product.Product;
import net.caiban.domain.product.ProductCategory;
import net.caiban.domain.product.ProductImage;
import net.caiban.dto.ExtResult;
import net.caiban.dto.ExtTreeNode;
import net.caiban.dto.Paging;
import net.caiban.dto.product.ProductDto;
import net.caiban.service.product.ProductCategoryService;
import net.caiban.service.product.ProductImageService;
import net.caiban.service.product.ProductService;
import net.caiban.util.StringUtil;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Mays (x03570227@gmail.com)
 */
@Scope("prototype")
@Service
public class ProductAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2781099235686160207L;
	
	private ExtResult result;
	private Paging page;
	private Product product;
	private ProductCategory productCategory;
	private String productArrays;
	private ProductCategory category;
	private List<ExtTreeNode> treeNodeList;
	private ProductDto productDto;
	private File doc;
	private String fileName;
	private String contentType;
	private String imageArrays;
	
	final static String UPLOAD_MODEL = "upload.product";
	final static String UPLOAD_PATH = "/product/";
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductImageService productImageService;
	
	public ProductAction(){
		result = new ExtResult();
		page = new Paging();
		product =new Product();
		productCategory = new ProductCategory();
		treeNodeList = new ArrayList<ExtTreeNode>();
		productDto = new ProductDto();
	}
	
	/**
	 * 转到产品中心页面中去
	 * @return
	 */
	public String index(){
		product.setUid(getSessionAuthUser().getId());
		return "index";
	}
	
	public String listProductCategoryByParent(){
		treeNodeList=productCategoryService.changeListToTreeNode(
						productCategoryService.listProductCategoryViaParent(
								productCategory.getParentId(),getSessionEshop().getId()));
		return "exttree";
	}
	
	public String listOneProductCategoryById(){
		List<ProductCategory> list=new ArrayList<ProductCategory>();
		list.add(productCategoryService.listOneProductCategoryViaId(productCategory.getId()));
		page.setRecords(list);
		return "extpage";
	}
	
	public String createProductCategory(){
		
		productCategory.setUid(getSessionAuthUser().getId());
		productCategory.setEid(getSessionEshop().getId());
		
		if(productCategoryService.createProductCategory(productCategory)>0){
			result.setSuccess(true);
		}
		
		return "extresult";
	}
	
	public String updateProductCategory(){
		productCategory.setEid(getSessionEshop().getId());
		productCategoryService.updateProductCategoryViaId(productCategory);
		result.setSuccess(true);
		
		return "extresult";
	}
	
	public String deleteProductCategory(){
		if(productCategoryService.deleteProductCategory(productCategory.getId())>0);
		result.setSuccess(true);
		return "extresult";
	}
	
	public String deleteProductImage(){
		if(productImageService.batchDeleteImageByIdArray(StringUtil.str2intArray(imageArrays))>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	/**
	 * 清空某个类别的所有产品信息
	 * @return
	 */
	public String emptyProductByCategoryId(){
		return "extresult";
	}
	
	public String listProduct(){
		
		product.setEid(getSessionEshop().getId());
		
		productDto.setProduct(product);
		productDto.setProductCategory(productCategory);
		
		page = productService.pageProductDto(productDto, page);
		return "extpage";
	}
	
	public String listImageByProduct(){
		page.setRecords(productImageService.listImageByProductId(product.getId()));
		return "extpage";
	}
	
	public String listOneProduct(){
		List<ProductDto> dtoList = new ArrayList<ProductDto>();
		dtoList.add(productService.listOneProductDtoById(product.getId()));
		page.setRecords(dtoList);
		return "extpage";
	}
	
	public String createProduct(){
		product.setEid(getSessionEshop().getId());
		product.setUid(getSessionAuthUser().getId());
		
		productDto.setProduct(product);
		//设置图片
		int id=productService.createProduct(productDto);
		if(id>0){
			result.setSuccess(true);
			result.setData(id);
		}
		return "extresult";
	}
	
	public String updateProduct(){
		productService.updateProduct(product);
		result.setSuccess(true);
		return "extresult";
	}
	
	public String batchDeleteProduct(){
		if(productService.batchDeleteProductByIdArray(StringUtil.str2intArray(productArrays))>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String uploadImage() throws IOException {
		String targetDirectory = CacheConstFacade.getStringValue(UPLOAD_MODEL)+getSessionEshop().getId()+"/";
        String targetFileName = StringUtil.generateFileName(fileName);   
        File target = new File(targetDirectory, targetFileName);   
        ProductImage image=new ProductImage();
        try {
			FileUtils.copyFile(doc, target);
			result.setSuccess(true);
			
			//如果是修改产品信息时上传图片,则需要同时增加图片记录
			image.setFilename(targetFileName);
			image.setFiletype(contentType);
			image.setFilepath(UPLOAD_PATH+getSessionEshop().getId()+"/");
			image.setRemark(fileName);
			
			if(product.getId()!=null && product.getId()>0){
				image.setEid(getSessionEshop().getId());
				image.setUid(getSessionAuthUser().getId());
				image.setProductId(product.getId());
				Integer i=productImageService.createImage(image);
				image.setId(i);
			}
			
			result.setData(image);
		} catch (IOException e) {
			result.setData(e.getMessage());
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();  
		response.setContentType("text/html;charset=UTF-8");  
		
//			HttpServletResponse response = ServletActionContext.getResponse();
		StringBuffer sb=new StringBuffer();
		sb.append("{success:true}");
//			sb.append("{success:true,data:{")
//				.append("filename:'").append(image.getFilename()).append("',")
//				.append("filetype:'").append(image.getFiletype()).append("',")
//				.append("filepath:'").append(image.getFilepath()).append("'}}");
//			String msg = "{success:true,data:'"+result.getData().toString()+"'}";
		response.getWriter().print(sb.toString());
		return NONE;
	}
	
	
	
	/**
	 * @return the result
	 */
	public ExtResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ExtResult result) {
		this.result = result;
	}

	/**
	 * @return the page
	 */
	public Paging getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Paging page) {
		this.page = page;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the productArrays
	 */
	public String getProductArrays() {
		return productArrays;
	}

	/**
	 * @param productArrays the productArrays to set
	 */
	public void setProductArrays(String productArrays) {
		this.productArrays = productArrays;
	}

	/**
	 * @return the category
	 */
	public ProductCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	/**
	 * @return the productCategory
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * @return the treeNodeList
	 */
	public List<ExtTreeNode> getTreeNodeList() {
		return treeNodeList;
	}

	/**
	 * @param treeNodeList the treeNodeList to set
	 */
	public void setTreeNodeList(List<ExtTreeNode> treeNodeList) {
		this.treeNodeList = treeNodeList;
	}

	/**
	 * @return the productDto
	 */
	public ProductDto getProductDto() {
		return productDto;
	}

	/**
	 * @param productDto the productDto to set
	 */
	public void setProductDto(ProductDto productDto) {
		this.productDto = productDto;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setDocFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setDocContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the doc
	 */
	public File getDoc() {
		return doc;
	}

	/**
	 * @param doc the doc to set
	 */
	public void setDoc(File doc) {
		this.doc = doc;
	}

	/**
	 * @return the imageArrays
	 */
	public String getImageArrays() {
		return imageArrays;
	}

	/**
	 * @param imageArrays the imageArrays to set
	 */
	public void setImageArrays(String imageArrays) {
		this.imageArrays = imageArrays;
	}
	
	
}
