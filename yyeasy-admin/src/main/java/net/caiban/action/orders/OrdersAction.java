/**
 * 
 */
package net.caiban.action.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.caiban.action.BaseAction;
import net.caiban.domain.auth.Param;
import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.domain.orders.Logistics;
import net.caiban.domain.orders.LogisticsDetails;
import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.OrdersProduct;
import net.caiban.domain.orders.SaleDispute;
import net.caiban.domain.product.Product;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.OrdersDto;
import net.caiban.dto.product.ProductDto;
import net.caiban.facade.ParamFacade;
import net.caiban.service.eshop.EshopBuyerService;
import net.caiban.service.orders.LogisticsService;
import net.caiban.service.orders.OrdersProductService;
import net.caiban.service.orders.OrdersService;
import net.caiban.service.product.ProductService;
import net.caiban.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author mays(x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class OrdersAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7467013959180224949L;
	
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private EshopBuyerService eshopBuyerService;
	@Autowired
	private ProductService productService;
	@Autowired 
	private OrdersProductService ordersProductService;
	@Autowired
	private LogisticsService logisticsService;

	private Orders orders;
	private Paging page;
	private ExtResult result;
	private OrdersDto ordersDto;
	private EshopBuyer eshopBuyer;
	private String query;
	private OrdersProduct ordersProduct;
	private String ordersArray;
	private LogisticsDetails logisticsDetails;
	private SaleDispute saleDispute;
	private List<Param> paramList;
	private String keyArray;
	
	private String onlyme;
	
	final static String COMBO_PAYMENT_KEY = "orders_payment";
	final static String ORDER_STATUS_KEY = "orders_status";
	final static int DEFAULT_COMBO_SIZE = 100;

	public OrdersAction(){
		ordersDto = new OrdersDto();
		orders = new Orders();
		page = new Paging();
		result = new ExtResult();
		eshopBuyer = new EshopBuyer();
		ordersProduct = new OrdersProduct();
		logisticsDetails = new LogisticsDetails();
		saleDispute = new SaleDispute();
	}
	
	public String index(){
		paramList = ParamFacade.getInstance().getParamListByType(ORDER_STATUS_KEY);
		return  "index";
	}
	
	public String listOrders(){
		if("Y".equals(onlyme)){
			orders.setUid(getSessionAuthUser().getId());
		}
		if(StringUtil.isNotEmpty(keyArray)){
			ordersDto.setOrderStatusArray(StringUtil.str2shortArray(keyArray));
		}
		orders.setEid(getSessionEshop().getId());
		ordersDto.setOrders(orders);
		page = ordersService.pageOrdersDto(ordersDto, page);
		return "extpage";
	}
	
	public String listOneOrders(){
//		orders.setEid(getSessionEshop().getId());
		List<OrdersDto> list=new ArrayList<OrdersDto>();
		list.add(ordersService.listOneOrdersById(orders.getId()));
		page.setRecords(list);
		return "extpage";
	}
	
	public String deleteOrders(){
		result.setData(ordersService.batchDeleteOrders(StringUtil.str2intArray(ordersArray)));
		result.setSuccess(true);
//		return "extresult";
		return EXTRESULT;
	}
	
	public String createNewOrders(){
		orders.setEid(getSessionEshop().getId());
		orders.setUid(getSessionAuthUser().getId());
		if(orders.getBuyerId() == null || orders.getBuyerId()==0){
			eshopBuyer.setEid(getSessionEshop().getId());
			eshopBuyer.setUid(getSessionAuthUser().getId());
			orders.setBuyerId(eshopBuyerService.createEshopBuyer(eshopBuyer));
		} 
		Integer id = ordersService.createSimpleOrders(orders);
		if(id!=null || id >0 ){
			result.setSuccess(true);
			result.setData(id);
		}
		return "extresult";
	}
	
	public String addOrdersProduct(){
		Integer id = ordersProductService.createOrdersProduct(ordersProduct);
		if(id>0){
			result.setSuccess(true);
			result.setData(id);
		}
		return "extresult";
	}
	
	public String paymentCombo(){
		page.setRecords(ParamFacade.getInstance().getParamListByType(COMBO_PAYMENT_KEY));
		return "extpage";
	}
	
	public String eshopBuyerCombo(){
		if(!StringUtil.isEmpty(query)){
			page.setRecords(eshopBuyerService.listEshopBuyerByName(query, 0, getSessionEshop().getId(), getSessionAuthUser().getId()));
		}
		return "extpage";
	}
	
	public String productCombo(){
		if(!StringUtil.isEmpty(query)){
			ProductDto p = new ProductDto();
			p.setProduct(new Product());
			p.getProduct().setEid(getSessionEshop().getId());
//			p.getProduct().setUid(getSessionAuthUser().getId());  //根据实际需要加
			p.getProduct().setName(query);
			page.setLimit(DEFAULT_COMBO_SIZE);
			page = productService.pageProductDto(p, page);
		}
		return "extpage";
	}
	
	public String logisticsCombo(){
		if(!StringUtil.isEmpty(query)){
			Logistics l = new Logistics();
			l.setEid(getSessionEshop().getId());
			l.setName(query);
			page.setLimit(DEFAULT_COMBO_SIZE);
			page = logisticsService.pageLogisticsWithEid(l, page);
		}
		return "extpage";
	}
	
	public String loadProductListByOrdersId(){
		page.setRecords(ordersProductService.listOrdersProductByOrdersId(orders.getId()));
		return "extpage";
	}
	
	public String updateSimpleOrders(){
		//TODO 只允许更新特定条件下的订单（一旦发货，将无法更改）
		ordersService.updateSimpleOrders(orders);
		ordersProductService.clearOrdersProductByOrdersId(orders.getId());
		result.setSuccess(true);
		result.setData(orders.getId());
		return "extresult";
	}
	
	public String createLogisticsDetails(){
		logisticsDetails.setEid(getSessionEshop().getId());
		logisticsDetails.setUid(getSessionAuthUser().getId());
		if(ordersService.deliveryOrders(logisticsDetails)>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String arrivalConfirm(){
		logisticsDetails.setEid(getSessionEshop().getId());
		logisticsDetails.setGmtReceive(new Date());
		Integer i = ordersService.arrivalConfirm(logisticsDetails);
		if(i!=null && i>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String createSaleDispute(){
		saleDispute.setEid(getSessionEshop().getId());
		saleDispute.setUid(getSessionAuthUser().getId());
		Integer i = ordersService.failureOrders(saleDispute);
		if(i!=null && i>0){
			result.setSuccess(true);
		}
		return "extresult"; 
	}

	/**
	 * @return the orders
	 */
	public Orders getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(Orders orders) {
		this.orders = orders;
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
	 * @return the eshopBuyer
	 */
	public EshopBuyer getEshopBuyer() {
		return eshopBuyer;
	}

	/**
	 * @param eshopBuyer the eshopBuyer to set
	 */
	public void setEshopBuyer(EshopBuyer eshopBuyer) {
		this.eshopBuyer = eshopBuyer;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the ordersProduct
	 */
	public OrdersProduct getOrdersProduct() {
		return ordersProduct;
	}

	/**
	 * @param ordersProduct the ordersProduct to set
	 */
	public void setOrdersProduct(OrdersProduct ordersProduct) {
		this.ordersProduct = ordersProduct;
	}

	/**
	 * @return the ordersArray
	 */
	public String getOrdersArray() {
		return ordersArray;
	}

	/**
	 * @param ordersArray the ordersArray to set
	 */
	public void setOrdersArray(String ordersArray) {
		this.ordersArray = ordersArray;
	}

	/**
	 * @return the onlyme
	 */
	public String getOnlyme() {
		return onlyme;
	}

	/**
	 * @param onlyme the onlyme to set
	 */
	public void setOnlyme(String onlyme) {
		this.onlyme = onlyme;
	}

	/**
	 * @return the logisticsDetails
	 */
	public LogisticsDetails getLogisticsDetails() {
		return logisticsDetails;
	}

	/**
	 * @param logisticsDetails the logisticsDetails to set
	 */
	public void setLogisticsDetails(LogisticsDetails logisticsDetails) {
		this.logisticsDetails = logisticsDetails;
	}

	/**
	 * @return the saleDispute
	 */
	public SaleDispute getSaleDispute() {
		return saleDispute;
	}

	/**
	 * @param saleDispute the saleDispute to set
	 */
	public void setSaleDispute(SaleDispute saleDispute) {
		this.saleDispute = saleDispute;
	}

	/**
	 * @return the paramList
	 */
	public List<Param> getParamList() {
		return paramList;
	}

	/**
	 * @param paramList the paramList to set
	 */
	public void setParamList(List<Param> paramList) {
		this.paramList = paramList;
	}

	/**
	 * @return the keyArray
	 */
	public String getKeyArray() {
		return keyArray;
	}

	/**
	 * @param keyArray the keyArray to set
	 */
	public void setKeyArray(String keyArray) {
		this.keyArray = keyArray;
	}
	
	
}
