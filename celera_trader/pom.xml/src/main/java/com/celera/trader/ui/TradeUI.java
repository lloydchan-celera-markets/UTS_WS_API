package com.celera.trader.ui;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EOrderType;
import com.celera.core.dm.ESide;
import com.celera.core.dm.ETradeReportType;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.ITrade;
import com.celera.core.dm.ITradeReport;
import com.celera.core.dm.Order;
import com.celera.core.dm.TradeReport;
import com.celera.core.oms.IOMSListener;
import com.celera.core.oms.OMS;
import com.celera.gateway.HkexOapiGateway;
import com.celera.gateway.IOrderGateway;
import com.celera.gateway.OrderGatewayManager;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class TradeUI extends Application implements IOMSListener {
 
	private static final Logger logger = LoggerFactory.getLogger(TradeUI.class);
	
    private TableView<DisplayOrder> table = new TableView<DisplayOrder>();
    private final ObservableList<DisplayOrder> data =
            FXCollections.observableArrayList();
//    private TableView<Person> table = new TableView<Person>();
//    private final ObservableList<Person> data =
//            FXCollections.observableArrayList(
//            new Person("Jacob", "Smith", "jacob.smith@example.com"),
//            new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
//            new Person("Ethan", "Williams", "ethan.williams@example.com"),
//            new Person("Emma", "Jones", "emma.jones@example.com"),
//            new Person("Michael", "Brown", "michael.brown@example.com"));
    
    final HBox hb = new HBox();
 
	OrderGatewayManager gwm = OrderGatewayManager.instance();

//	HkexOapiGateway gw = new HkexOapiGateway();
//	gw.init();
//	gw.start();
	
	IOrderGateway gw = gwm.getOrderGateway(HkexOapiGateway.class.getName());
	
	Map<Long, DisplayOrder> displayMap = new HashMap<Long, DisplayOrder>();
	Map<Long, IOrder> map = new HashMap<Long, IOrder>();
	Map<Long, ArrayList<IOrder>> blocks = new HashMap<Long, ArrayList<IOrder>>();

	IOrder order = null;

	OMS oms = OMS.instance();;
	
	public TradeUI() {
	}
	
    public static void main(String[] args) {
        launch(args);
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void start(Stage stage) {
    	
    	TradeUI ui = new TradeUI();
    	
		gwm.init();
		gwm.start();
		oms.init();
		
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);

//        TableColumn firstNameCol = new TableColumn("First Name");
//        firstNameCol.setMinWidth(100);
//        firstNameCol.setCellValueFactory(
//            new PropertyValueFactory<Person, String>("firstName"));
//        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        firstNameCol.setOnEditCommit(
//            new EventHandler<CellEditEvent<Person, String>>() {
//                @Override
//                public void handle(CellEditEvent<Person, String> t) {
//                    ((Person) t.getTableView().getItems().get(
//                            t.getTablePosition().getRow())
//                            ).setFirstName(t.getNewValue());
//                }
//            }
//        );
// 
// 
//        TableColumn lastNameCol = new TableColumn("Last Name");
//        lastNameCol.setMinWidth(100);
//        lastNameCol.setCellValueFactory(
//            new PropertyValueFactory<Person, String>("lastName"));
//        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        lastNameCol.setOnEditCommit(
//            new EventHandler<CellEditEvent<Person, String>>() {
//                @Override
//                public void handle(CellEditEvent<Person, String> t) {
//                    ((Person) t.getTableView().getItems().get(
//                        t.getTablePosition().getRow())
//                        ).setLastName(t.getNewValue());
//                }
//            }
//        );
// 
//        TableColumn emailCol = new TableColumn("Email");
//        emailCol.setMinWidth(200);
//        emailCol.setCellValueFactory(
//            new PropertyValueFactory<Person, String>("email"));
//        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        emailCol.setOnEditCommit(
//            new EventHandler<CellEditEvent<Person, String>>() {
//                @Override
//                public void handle(CellEditEvent<Person, String> t) {
//                    ((Person) t.getTableView().getItems().get(
//                        t.getTablePosition().getRow())
//                        ).setEmail(t.getNewValue());
//                }
//            }
//        );
// 
//        table.setItems(data);
//        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
// 
//        final TextField addFirstName = new TextField();
//        addFirstName.setPromptText("First Name");
//        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
//        final TextField addLastName = new TextField();
//        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
//        addLastName.setPromptText("Last Name");
//        final TextField addEmail = new TextField();
//        addEmail.setMaxWidth(emailCol.getPrefWidth());
//        addEmail.setPromptText("Email");
//        
//        final Button addButton = new Button("Add");
//        addButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                data.add(new DisplayOrder(99l, EOrderStatus.PENDING_NEW));
//            }
//        });
        
        TableColumn firstNameCol = new TableColumn("Order ID");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
            new PropertyValueFactory<DisplayOrder, String>("ordId"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<DisplayOrder, String>>() {
                @Override
                public void handle(CellEditEvent<DisplayOrder, String> t) {
                    ((DisplayOrder) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setOrdId(Long.parseLong(t.getNewValue()));
                }
            }
        );
 
		TableColumn lastNameCol = new TableColumn("Status");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
            new PropertyValueFactory<DisplayOrder, String>("status"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<DisplayOrder, String>>() {
                @Override
                public void handle(CellEditEvent<DisplayOrder, String> t) {
                    ((DisplayOrder) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setStatus(EOrderStatus.get(t.getNewValue()));
                }
            }
        );
 
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol);
 
        
        // ================ order =========================
        final TextField command = new TextField();
        command.setPromptText("Command");
        command.setMinWidth(200);
        
        final Button submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
//                data.add(new Person("addFirst", "addLast", "addEmail"));
            	onSubmit(command.getText());
            }
        });
        
        // =================== admin ======================
        ObservableList<String> options = 
        	    FXCollections.observableArrayList(
        	        "LOGIN",
        	        "LOGOUT",
        	        "SOD",
        	        "SUBSCRIBE",
        	        "QUERY",
//        	        "CLOSE",
        	        "GET_INSTRUMENT"
        	    );
        final ComboBox adminCb = new ComboBox(options);
        
        final TextField adminCmd = new TextField();
        adminCmd.setText("geniumtesting");
        adminCmd.setMinWidth(100);
        
        final Button adminButton = new Button("Admin");
        adminButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		ui.onAdmin(adminCb.getValue().toString(), adminCmd.getText());
        	}
        });

//        final VBox vbox = new VBox();
//        vbox.setSpacing(5);
//        vbox.setPadding(new Insets(10, 0, 0, 10));
//        vbox.getChildren().addAll(table, addButton);
 
//      ((Group) scene.getRoot()).getChildren().addAll(vbox);
        
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Admin: "), 0, 0);
        grid.add(adminCb, 1, 0);
        grid.add(adminCmd, 2, 0);
        grid.add(adminButton, 3, 0);
        grid.add(new Label("Order: "), 0, 1);
        grid.add(command, 1, 1, 2, 1);            
        grid.add(submitButton, 3, 1);            
        
//        grid.add(vbox, 0, 2, 4, 2);
        grid.add(table, 0, 2, 4, 2);
//        grid.add(addButton, 0, 3);
        
        Scene scene = new Scene(new Group(), 800, 500);
        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);


        stage.setTitle("Trade UI");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setScene(scene);
        stage.show();
    }
 
    public void onAdmin(String admin, String line) {
    	
    	String msg = "";
    	
    	try {
    	
		String[] tokens = line.split(",");
		switch (admin.toUpperCase())
		{
		case "START": // 1) HI,geniumtesting
		{
//			gw.init();
			gw.start();
//			gw.start(tokens[1]);
			break;
		}
		case "LOGIN": // 1) HI,geniumtesting
		{
			gw.login(tokens[0]);
			break;
		}
		case "SOD":
		{ // 2) HS
			gw.SOD();
			break;
		}
		case "SUBSCRIBE":
		{ // 3) HM
			gw.subscribeMarketData();
			break;
		}
		case "LOGOUT": // 4) HO
		{ // optional) Logout
			gw.logout();
			break;
		}
		case "CLOSE": // 4) HO
		{ // optional) Logout
			gw.stop();
			break;
		}
		case "CHANGE_PASSWORD": // 5) HC,password,newPassword
		{ // optional) Logout
			String password = tokens[0];
			String newPassword = tokens[1];
			gw.changePassword(password, newPassword);
			break;
		}
		case "GET_INSTRUMENT": // get all instrument from hkex oapi
		{
			gw.getAllInstrument();
			break;
		}
		case "QUERY": // 5) HC,password,newPassword
		{ // optional) Logout
			String command = tokens[0];
			gw.query(command);
			break;
		}
		default:
			logger.error("Admin command not found admin[{}] line[{}]", admin, line);
			break;
		}
    	}
		catch (Exception e) 
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Admin[" + admin + "] Command[" + line + "]");
//			alert.setContentText(msg);

			alert.showAndWait();
			
			logger.error("", e);
		}
    }
    
    public void onSubmit(String line) {
//		String line = sc.nextLine();
		String[] tokens = line.split(",");
		switch (tokens[0].toUpperCase())
		{
		case "ON":
		{
			try
			{
				String symbol = tokens[1];
				Long orderId = Long.parseLong(tokens[2]);
				Double price = Double.parseDouble(tokens[3]);
				Integer qty = Integer.parseInt(tokens[4]);
				String side = tokens[5].toUpperCase();
				Double strike = Double.parseDouble(tokens[6]);
				String giveup = null;
				try {
					giveup = tokens[7].toUpperCase();
				}
				catch (Exception e) {
				}
				com.celera.core.dm.ESessionState state = com.celera.core.dm.ESessionState.get(tokens[8]);
				// String month = tokens[7].toUpperCase();
				// Double delta = Double.parseDouble(tokens[8]);

				IInstrument instr = new Derivative("HK", symbol, EInstrumentType.EP, "European Put", null,
						null, null, strike, "", null, false, 0d);
				// IInstrument instr = new Derivative("HK", symbol,
				// EInstrumentType.EP, "European Put",
				// null, null, null, strike, month, null, false,
				// delta);
//				String giveup = "CTOM";
				order = new Order(EOrderStatus.NEW, instr, EOrderType.LIMIT, orderId, new Date().getTime(), "", price, qty,
						ESide.get(side), giveup, state);
				oms.sendOrder(order);
				map.put(orderId, order);
				
				// update table view 
				this.onOrder(order);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		case "OM": // modify order OM,1,438,105 OM.orderId,price,qty
		{
			try
			{
				Long orderId = Long.parseLong(tokens[1]);
				order = map.get(orderId);
				Double price = Double.parseDouble(tokens[2]);
				Integer qty = Integer.parseInt(tokens[3]);
				order.setOrderId(orderId);
				order.setPrice(price);
				order.setQty(qty);
				order.setStatus(EOrderStatus.AMEND);
				gw.modifyOrder(order);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		case "OC": // cancel order OC,1 OC,orderId
		{
			try
			{
				Long orderId = Long.parseLong(tokens[1]);
				order = map.get(orderId);
				order.setOrderId(orderId);
				order.setStatus(EOrderStatus.CANCELLED);
				gw.cancelOrder(order);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		// TN,symbol,order_id,price,quantity,side,trade report type,my comapny,counter party company
		case "TN":	// TN,HHI9800O7,2,439,100,buy,T4,HKTOM    (interbank cross T4)
		{
			try
			{
				int pos = 1;
				String symbol = tokens[pos++];
				Long refId = new Date().getTime();
				Double price = Double.parseDouble(tokens[pos++]);
				Integer qty = Integer.parseInt(tokens[pos++]);
				String trType = tokens[pos++].toUpperCase();
				String myCompany = tokens[pos++];
				String cpCompany = tokens[pos++];

				IInstrument instr = new Derivative("HK", symbol, EInstrumentType.EP, "European Put", null,
						null, null, null, "", null, false, 0d);
				// IInstrument instr, EOrderStatus status,
				// ETradeReportType tradeReportType,
				// Integer qty, Double price, Long id, ESide
				// side, String company
				ITradeReport tr = new TradeReport(instr, EOrderStatus.NEW, ETradeReportType.get(trType),
						ESide.CROSS, qty, price, null, refId, myCompany, cpCompany);
				tr.setGroupId(1l);
				oms.sendTradeReport(tr);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		case "T2":	// T2,2,3,439,100,buy,HSI22000L7,HSI24000L7,HSIJ7    (interbank cross T4)
		{
			int pos = 1;
			try
			{
				Long groupId = Long.parseLong(tokens[pos++]);
				Integer numLegs = Integer.parseInt(tokens[pos++]);
				Double legPrice = Double.parseDouble(tokens[pos++]);
				Integer legQty = Integer.parseInt(tokens[pos++]);
				String sTrType = tokens[pos++];
//				ETradeReportType trtype = ETradeReportType.get(sTrType);
				
				Long refId = new Date().getTime();
				
				IInstrument instr = new Derivative("HK", "SYHN", EInstrumentType.ECDIAG,
						EInstrumentType.ECDIAG.getName(), null, null, null, null, "", null, false, 0d);
				BlockTradeReport block = new BlockTradeReport(instr, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
						legQty, legPrice, null, refId, "HKCEL", "HKCEL");
				block.setGroupId(groupId);
				
				String legSymbol = null;
				Map<Long, java.util.List<ITradeReport>> split = new HashMap<Long, java.util.List<ITradeReport>>();
				
					for (int i=0; i<numLegs; i++) {
						legSymbol = tokens[pos++];
						groupId = Long.parseLong(tokens[pos++]);
						legPrice = Double.parseDouble(tokens[pos++]);
						legQty = Integer.parseInt(tokens[pos++]);
					EInstrumentType legTrType = EInstrumentType.bySymbol(legSymbol);
					instr = new Derivative("HK", legSymbol, legTrType, legTrType.getName(),
							null, null, null, null, "", null, false, 0d);
					ITradeReport tr = new TradeReport(instr, EOrderStatus.PENDING_NEW,
							ETradeReportType.T2_COMBO_CROSS, ESide.CROSS, legQty, legPrice, null, refId,
							"HKCEL", "HKCEL");
					tr.setGroupId(groupId);
					
					ArrayList<ITradeReport> l = (ArrayList<ITradeReport>) split.get(groupId);
					if (l == null) {
						l = new ArrayList<ITradeReport>();
						split.put(groupId, l);
					}
					l.add(tr);
				}
					
//					IOrderGateway gw1 = og.getOrderGateway(legSymbol);
//				gw.createBlockTradeReport(block);
				oms.sendBlockTradeReport(block, split);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		case "TC": // cancel order OC,1 OC,orderId
		{
			try
			{
				Long orderId = Long.parseLong(tokens[1]);
				order = map.get(orderId);
				order.setOrderId(orderId);
				gw.cancelTradeReport(order);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
//		case "START": // 1) HI,geniumtesting
//		{
//			gw.start();
//			break;
//		}
//		case "LOGIN": // 1) HI,geniumtesting
//		{
//			gw.login(tokens[1]);
//			break;
//		}
//		case "SOD":
//		{ // 2) HS
//			gw.SOD();
//			break;
//		}
//		case "SUBSCRIBE":
//		{ // 3) HM
//			gw.subscribeMarketData();
//			break;
//		}
//		case "LOGOUT": // 4) HO
//		{ // optional) Logout
//			gw.logout();
//			break;
//		}
//		case "CLOSE": // 4) HO
//		{ // optional) Logout
//			gw.stop();
//			break;
//		}
//		case "CHANGE_PASSWORD": // 5) HC,password,newPassword
//		{ // optional) Logout
//			String password = tokens[1];
//			String newPassword = tokens[2];
//			gw.changePassword(password, newPassword);
//			break;
//		}
//		case "GET_INSTRUMENT": // get all instrument from hkex oapi
//		{
//			gw.getAllInstrument();
//			break;
//		}
//		case "QUERY": // 5) HC,password,newPassword
//		{ // optional) Logout
//			String command = tokens[1];
//			gw.query(command);
//			break;
//		}
		default:
		{
			break;
		}
		}
    }
    
    public static class DisplayOrder {
 
        private final SimpleStringProperty ordId;
        private final SimpleStringProperty status;
 
        private DisplayOrder(Long ordId, EOrderStatus status) {
            this.ordId = new SimpleStringProperty(ordId.toString());
            this.status = new SimpleStringProperty(status.getName());
        }
 
        public String getOrdId()
		{
			return ordId.get();
		}
        
        public void setOrdId(Long ordId) {
        	this.ordId.set(ordId.toString());
        }
        
		public String getStatus()
		{
			return status.get();
		}

		public void setStatus(EOrderStatus status) {
        	this.status.set(status.getName());
        }
    }


	@Override
	public void onOrder(IOrder o)
	{
		long ordId = o.getOrderId();
		DisplayOrder old = displayMap.get(ordId);
		if (old == null) {
//		if (map.containsKey(ordId)) {

			DisplayOrder display = new DisplayOrder(ordId, o.getStatus());
			data.add(display);
//            data.add(new Person("addFirstName","addLastName","addEmail"));
            logger.debug("add display person {}", data.size());
		}
		else {
			old.setStatus(o.getStatus());
		}
	}

	@Override
	public void onQuote(IOrder q)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrade(ITrade t)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTradeReport(ITradeReport tr)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInstrumentUpdate(IInstrument i)
	{
		// TODO Auto-generated method stub
		
	}
	
    public static class Person {
    	 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
 
        private Person(String fName, String lName, String email) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
        }
 
        public String getFirstName() {
            return firstName.get();
        }
 
        public void setFirstName(String fName) {
            firstName.set(fName);
        }
 
        public String getLastName() {
            return lastName.get();
        }
 
        public void setLastName(String fName) {
            lastName.set(fName);
        }
 
        public String getEmail() {
            return email.get();
        }
 
        public void setEmail(String fName) {
            email.set(fName);
        }
    }
}