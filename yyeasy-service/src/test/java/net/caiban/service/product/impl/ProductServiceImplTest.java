package net.caiban.service.product.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.caiban.domain.product.Product;
import net.caiban.domain.product.ProductCategory;
import net.caiban.domain.product.ProductImage;
import net.caiban.dto.Paging;
import net.caiban.dto.product.ProductDto;
import net.caiban.service.product.ProductService;
import net.caiban.util.StringUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceImplTest extends ProductCategoryServiceImplTest{

	@Autowired
	private ProductService productService;
	
	int TEST_DATA = 5;
	String TEST_CATEGORY_NAME ="test category";

	@Test
	public void testBatchDeleteProductByIdArray_null_argument() {
		try {
			productService.batchDeleteProductByIdArray(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testBatchDeleteProductByIdArray() throws SQLException {
		clean();
		List<Integer> ids=prepareProduct();
		
		Integer impact = productService.batchDeleteProductByIdArray(StringUtil.str2intArray(ids.toString().replace("[","").replace("]","").replace(" ", "")));
		assertEquals(impact.intValue(), ids.size());
		
	}

	@Test
	public void testCreateProduct_null_argument() {
		try {
			productService.createProduct(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			productService.createProduct(new ProductDto());
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			ProductDto productDto =new ProductDto();
			productDto.setProduct(new Product());
			productService.createProduct(productDto);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			ProductDto productDto =new ProductDto();
			Product p=new Product();
			p.setUid(DEFAULT_UID);
			productDto.setProduct(p);
			productService.createProduct(productDto);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			ProductDto productDto =new ProductDto();
			Product p=new Product();
			p.setEid(DEFAULT_EID);
			productDto.setProduct(new Product());
			productService.createProduct(productDto);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
	}
	
	@Test
	public void testCreateProduct() throws SQLException {
		clean();
		Integer cid=create(new ProductCategory(null,"test category",0,1,2,DEFAULT_DEL,null,DEFAULT_UID,DEFAULT_EID));
		ProductDto productDto = new ProductDto();
		Product product =new Product(null,cid,"test product","remark","",0.0f,0.1f,"","",null,DEFAULT_UID,DEFAULT_EID);
		productDto.setProduct(product);
		productDto.setImageList(submitImageList(5));
		
		Integer id=productService.createProduct(productDto);
		assertTrue(id>0);
		
		List<ProductImage> imagelist=queryImageByProduct(id);
		assertEquals(imagelist.size(),5);
		
	}

	@Test
	public void testDeleteProductByCategoryId_null_argument() throws SQLException {
		try {
			productService.deleteProductByCategoryId(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	@Test
	public void testDeleteProductByCategoryId() throws SQLException {
		clean();
		prepareProduct();
		Integer cid = queryIdViaName(TEST_CATEGORY_NAME,DEFAULT_EID);
		
		Integer deled = productService.deleteProductByCategoryId(cid);
		assertEquals(deled.intValue(),TEST_DATA);
	}

	@Test
	public void testListOneProductDtoById_null_argument() {
		try {
			productService.listOneProductDtoById(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testListOneProductDtoById() throws SQLException {
		clean();
		Integer cid=create(new ProductCategory(null,"test category",0,1,1,DEFAULT_DEL,null,DEFAULT_UID,DEFAULT_EID));
		Integer id=createProduct(new Product(null,cid,"my product","","",0.0f,0.1f,"","",null,DEFAULT_UID,DEFAULT_EID));
		createImage(new ProductImage(null,id,"file1","","","",null,DEFAULT_UID,DEFAULT_EID));
		createImage(new ProductImage(null,id,"file2","","","",null,DEFAULT_UID,DEFAULT_EID));
		
		ProductDto productDto = productService.listOneProductDtoById(id);
		assertEquals(productDto.getProduct().getName(),"my product");
		assertEquals(productDto.getImageList().size(),2);
		//assertEquals(productDto.getProductCategory().getName(),"test category");
	}

	@Test
	public void testPageProductDto_null_argument() throws SQLException {
		try {
			productService.pageProductDto(null,null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			productService.pageProductDto(new ProductDto(),null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			productService.pageProductDto(new ProductDto(),new Paging());
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testPageProductDto() throws SQLException {
		clean();
		TEST_DATA = 12;
		prepareProduct();
		TEST_DATA=5;
		
		Paging page=new Paging();
		page.setLimit(10);

		Product p=new Product();
		p.setEid(DEFAULT_EID);
		ProductDto dto=new ProductDto();
		dto.setProduct(p);
		
		page.setStart(0);
		page=productService.pageProductDto(dto,page);
		assertEquals(page.getRecords().size(),10);
		assertEquals(page.getTotals().intValue(),12);
		
		page.setStart(10);
		page=productService.pageProductDto(dto,page);
		assertEquals(page.getRecords().size(),2);
		assertEquals(page.getTotals().intValue(),12);
		
	}
	
	@Test
	public void testUpdateProduct_null_argument() throws SQLException {
		try {
			productService.updateProduct(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			productService.updateProduct(new Product());
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testUpdateProduct() throws SQLException {
		clean();
		Integer cid=create(new ProductCategory(null,"test category",0,1,1,DEFAULT_DEL,null,DEFAULT_UID,DEFAULT_EID));
		Integer newcid=create(new ProductCategory(null,"test category2",0,1,1,DEFAULT_DEL,null,DEFAULT_UID,DEFAULT_EID));
		Integer id=createProduct(new Product(null,cid,"my product","","",0.0f,0.1f,"","",null,DEFAULT_UID,DEFAULT_EID));
		
		Product product =productService.listOneProductDtoById(id).getProduct();
		product.setProductCategoryId(newcid);
		product.setName("new product name");
		
		productService.updateProduct(product);
		
		Product newproduct =productService.listOneProductDtoById(id).getProduct();
		assertEquals(newproduct.getName(),"new product name");
		assertEquals(newproduct.getProductCategoryId(),newcid);
	}

	/***************data************/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from product_category").execute();
		connection.prepareStatement("delete from product_image").execute();
		connection.prepareStatement("delete from product").execute();
	}
	
	private List<Integer> prepareProduct() throws SQLException{
		Integer cid=create(new ProductCategory(null,TEST_CATEGORY_NAME,0,1,2,DEFAULT_DEL,null,DEFAULT_UID,DEFAULT_EID));
		List<Integer> impact=new ArrayList<Integer>();
		for(int i=0;i<TEST_DATA;i++){
			impact.add(createProduct(
				new Product(null,cid,"test product"+i,"","",0.0f,0.1f,"元","100*20",null,DEFAULT_UID,DEFAULT_EID)
			));
		}
		return impact;
	}
	
	private Integer createProduct(Product product) throws SQLException{
		String sql="";
		sql="insert into product(product_category_id,name,remark,model,price_purchase,price_sale,unit,size,gmtcreate,uid,eid) ";
		sql=sql+" values("+product.getProductCategoryId()+",'"+product.getName()+"','"+product.getRemark()+"','"+product.getModel()+"',"+product.getPricePurchase()
			+","+product.getPriceSale()+",'"+product.getUnit()+"','"+product.getSize()+"',now(),"+product.getUid()+","+product.getEid()+")";
		
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		
		return 0;
	}
	
	private List<ProductImage> submitImageList(Integer length){
		List<ProductImage> list=new ArrayList<ProductImage>();
		for(int i=0;i<length;i++){
			ProductImage image = new ProductImage();
			image.setEid(DEFAULT_EID);
			image.setUid(DEFAULT_UID);
			image.setFilename("testfilename"+i);
			image.setFiletype("image");
			image.setFilepath("/test/file/path/");
			list.add(image);
		}
		return list;
	}
	
	private List<ProductImage> queryImageByProduct(Integer productId) throws SQLException{
		ResultSet rs=connection.createStatement().executeQuery("select id,eid,uid,filename,gmtcreate,product_id from product_image where product_id="+productId);
		
		List<ProductImage> list=new ArrayList<ProductImage>();
		while(rs.next()){
			ProductImage image = new ProductImage();
			image.setId(rs.getInt("id"));
			image.setEid(rs.getInt("eid"));
			image.setUid(rs.getInt("uid"));
			image.setFilename(rs.getString("filename"));
			image.setGmtcreate(rs.getDate("gmtcreate"));
			image.setProductId(rs.getInt("product_id"));
			list.add(image);
		}
		return list;
	}
	
	private Integer createImage(ProductImage image) throws SQLException{
		String sql="";
		sql="insert into product_image(product_id,filename,uid,eid,gmtcreate) ";
		sql=sql+" values("+image.getProductId()+",'"+image.getFilename()+"',"+image.getUid()+","+image.getEid()+",now())";
		
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		
		return 0;
	}
	
}
