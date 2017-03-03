package com.celera.trader.ui;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.core.dm.BlockTradeReport;
import com.celera.core.dm.Derivative;
import com.celera.core.dm.EAdminAction;
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
import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.EFoCommand;
import com.celera.message.cmmf.EMessageType;
import com.celera.message.cmmf.ICmmfAdminService;
import com.celera.message.cmmf.ICmmfListener;
import com.celera.trader.webservice.TcpServer;
import com.sun.prism.impl.Disposer.Record;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HkexTradeUI extends Application implements IOMSListener, ICmmfListener
{
	public static class DisplayOrder
	{
		private final SimpleStringProperty ordId;
		private final SimpleStringProperty symbol;
		private final SimpleStringProperty type;
		private final SimpleStringProperty price;
		private final SimpleStringProperty qty;
		private final SimpleStringProperty status;
		private final SimpleStringProperty remark;

		private DisplayOrder(Long ordId, String symbol, String type, Double price, Integer qty, EOrderStatus status, String remark)
		{
			this.ordId = new SimpleStringProperty(ordId.toString());
			this.symbol = new SimpleStringProperty(symbol);
			this.type = new SimpleStringProperty(type);
			this.price = new SimpleStringProperty(price.toString());
			this.qty = new SimpleStringProperty(qty.toString());
			this.status = new SimpleStringProperty(status.getName());
			this.remark = new SimpleStringProperty(remark);
		}

		public String getType()
		{
			return type.get();
		}
		
		public String getRemark()
		{
			return remark.get();
		}
		
		public String getSymbol()
		{
			return symbol.get();
		}

		public String getOrdId()
		{
			return ordId.get();
		}

		public String getStatus()
		{
			return status.get();
		}

		public String getPrice()
		{
			return price.get();
		}

		public String getQty()
		{
			return qty.get();
		}
		
		public void setType(String type)
		{
			this.type.set(type);
		}
		
		public void setRemark(String remark)
		{
			this.remark.set(remark);
		}

		public void setSymbol(String symbol)
		{
			this.symbol.set(symbol);
		}
		
		public void setOrdId(Long ordId)
		{
			this.ordId.set(ordId.toString());
		}
		
		public void setStatus(EOrderStatus status)
		{
			this.status.set(status.getName());
		}

		public void setPrice(Double price)
		{
			if (price != null && price > 0)
				this.price.set(price.toString());
		}
		
		public void setQty(Integer qty)
		{
			if (qty != null && qty > 0)
				this.qty.set(qty.toString());
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(HkexTradeUI.class);

	private static final double WIDTH = 1000d;
	private static final double HEIGHT = 800d;
	
	private TableView<DisplayOrder> table = new TableView<DisplayOrder>();
	private final ObservableList<DisplayOrder> data = FXCollections.observableArrayList();
	// private TableView<Person> table = new TableView<Person>();
	// private final ObservableList<Person> data =
	// FXCollections.observableArrayList(
	// new Person("Jacob", "Smith", "jacob.smith@example.com"),
	// new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
	// new Person("Ethan", "Williams", "ethan.williams@example.com"),
	// new Person("Emma", "Jones", "emma.jones@example.com"),
	// new Person("Michael", "Brown", "michael.brown@example.com"));

	final HBox hb = new HBox();

	OrderGatewayManager gwm = OrderGatewayManager.instance();

	// HkexOapiGateway gw = new HkexOapiGateway();
	// gw.init();
	// gw.start();

	IOrderGateway gw = gwm.getOrderGateway(HkexOapiGateway.class.getName());

	Map<Long, DisplayOrder> displayMap = new HashMap<Long, DisplayOrder>();
	Map<Long, IOrder> map = new HashMap<Long, IOrder>();
	Map<Long, ArrayList<IOrder>> blocks = new HashMap<Long, ArrayList<IOrder>>();

	IOrder order = null;

	OMS oms = OMS.instance();;
	TcpServer server;
	
	public HkexTradeUI()
	{
//		new TcpServer(this);
		try
		{
			server = new TcpServer();
			server.setListener(this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args)
	{
		launch(args);
//       	HkexTradeUI ui = new HkexTradeUI();
//       	ui.start();
       	
       	while (true) {
       		try
			{
				Thread.sleep(100000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	}
	}

	public void start() {
		oms.addListener(this);
		
		gwm.init();
		gwm.start();
		oms.init();
	}
	
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	public void start(Stage stage)
	{
		start();

		final Label label = new Label("Address Book");
		label.setFont(new Font("Arial", 20));

		table.setEditable(true);

		TableColumn _1Col = new TableColumn("Order ID");
//		_1Col.setMinWidth(100);
		_1Col.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("ordId"));
		_1Col.setCellFactory(TextFieldTableCell.forTableColumn());
		_1Col.setEditable(false);
//		_1Col.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
//		{
//			@Override
//			public void handle(CellEditEvent<DisplayOrder, String> t)
//			{
//				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//						.setOrdId(Long.parseLong(t.getNewValue()));
//			}
//		});
		
		TableColumn _symCol = new TableColumn("Symbol");
//		_2Col.setMinWidth(100);
		_symCol.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("symbol"));
		_symCol.setCellFactory(TextFieldTableCell.forTableColumn());
		_symCol.setEditable(false);
//		_symCol.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
//		{
//			@Override
//			public void handle(CellEditEvent<DisplayOrder, String> t)
//			{
//				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//				.setSymbol(t.getNewValue());
//			}
//		});
		
		TableColumn _typeCol = new TableColumn("Type");
		_typeCol.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("type"));
		_typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
		_typeCol.setEditable(false);
//		_typeCol.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
//		{
//			@Override
//			public void handle(CellEditEvent<DisplayOrder, String> t)
//			{
//				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//				.setType(t.getNewValue());
//			}
//		});
		
		TableColumn _2Col = new TableColumn("Price");
//		_2Col.setMinWidth(100);
		_2Col.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("price"));
		_2Col.setCellFactory(TextFieldTableCell.forTableColumn());
		_2Col.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
		{
			@Override
			public void handle(CellEditEvent<DisplayOrder, String> t)
			{
				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
				.setPrice(Double.parseDouble(t.getNewValue()));
			}
		});
		
		TableColumn _3Col = new TableColumn("Qty");
//		_3Col.setMinWidth(100);
		_3Col.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("qty"));
		_3Col.setCellFactory(TextFieldTableCell.forTableColumn());
		_3Col.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
		{
			@Override
			public void handle(CellEditEvent<DisplayOrder, String> t)
			{
				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setQty(Integer.parseInt(t.getNewValue()));
			}
		});

		TableColumn _4Col = new TableColumn("Status");
//		_4Col.setMinWidth(100);
		_4Col.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("status"));
		_4Col.setCellFactory(TextFieldTableCell.forTableColumn());
		_4Col.setEditable(false);
//		_4Col.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
//		{
//			@Override
//			public void handle(CellEditEvent<DisplayOrder, String> t)
//			{
//				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//					.setStatus(EOrderStatus.get(t.getNewValue()));
//			}
//		});

		
		TableColumn _remarkCol = new TableColumn("Reason");
		_remarkCol.setPrefWidth(200);
		_remarkCol.setCellValueFactory(new PropertyValueFactory<DisplayOrder, String>("remark"));
		_remarkCol.setCellFactory(TextFieldTableCell.forTableColumn());
		_remarkCol.setEditable(false);
//		_remarkCol.setOnEditCommit(new EventHandler<CellEditEvent<DisplayOrder, String>>()
//		{
//			@Override
//			public void handle(CellEditEvent<DisplayOrder, String> t)
//			{
//				((DisplayOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//				.setRemark(t.getNewValue());
//			}
//		});
		
		// cancel button
        TableColumn cxl_action = new TableColumn<>("");
        cxl_action.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Record, Boolean>, 
                ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
        cxl_action.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
                return new CancelButtonCell();
            }
        });

        // modify button
        TableColumn mod_action = new TableColumn<>("");
        mod_action.setCellValueFactory(
        		new Callback<TableColumn.CellDataFeatures<Record, Boolean>, 
        		ObservableValue<Boolean>>() {
        			@Override
        			public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
        				return new SimpleBooleanProperty(p.getValue() != null);
        			}
        		});
        mod_action.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
        	@Override
        	public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
        		return new ModifyButtonCell();
        	}
        });
		
		table.setItems(data);
		table.getColumns().addAll(_1Col, _symCol, _typeCol, _2Col, _3Col, _4Col, _remarkCol, mod_action, cxl_action);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		// ================ order =========================
		final TextField command = new TextField();
		command.setPromptText("Command");
		command.setPrefWidth(WIDTH * .8);

		final Button submitButton = new Button("Submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				// data.add(new Person("addFirst", "addLast", "addEmail"));
				submit(command.getText());
			}
		});

		// =================== admin ======================
		ObservableList<String> options = FXCollections.observableArrayList(
				"LOGIN", "LOGOUT", "SOD", "SUBSCRIBE", "QUERY", /*"CLOSE",*/ "GET_INSTRUMENT", "CHANGE_PASSWORD", "SET_READY");
		final ComboBox adminCb = new ComboBox(options);

		final TextField adminCmd = new TextField();
		adminCmd.setText("geniumtesting");
//		adminCmd.setMinWidth(100);

		final Button adminButton = new Button("Admin");
		adminButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				onAdmin(adminCb.getValue().toString(), adminCmd.getText());
			}
		});

		// final VBox vbox = new VBox();
		// vbox.setSpacing(5);
		// vbox.setPadding(new Insets(10, 0, 0, 10));
		// vbox.getChildren().addAll(table, addButton);

		// ((Group) scene.getRoot()).getChildren().addAll(vbox);

		GridPane grid = new GridPane();
		grid.setPrefSize(WIDTH, HEIGHT);
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

		// grid.add(vbox, 0, 2, 4, 2);
		grid.add(table, 0, 2, 4, 2);
		// grid.add(addButton, 0, 3);

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().add(columnConstraints);
		
		Scene scene = new Scene(new Group(), WIDTH, HEIGHT);
		Group root = (Group) scene.getRoot();
		root.getChildren().add(grid);

		stage.setTitle("Trade UI");
//		stage.setWidth(1600);
//		stage.setHeight(400);
		stage.setScene(scene);
		stage.show();
	}

	public void onAdmin(String admin, String line)
	{
		String msg = "";

		try
		{
			String[] tokens = line.split(",");
			switch (admin.toUpperCase())
			{
			case "START": // 1) HI,geniumtesting
			{
				// gw.init();
				gw.start();
				// gw.start(tokens[1]);
				break;
			}
			case "LOGIN": // 1) HI,geniumtesting
			{
				((ICmmfAdminService)gw).login(tokens[0]);
				break;
			}
			case "SOD":
			{ // 2) HS
				((ICmmfAdminService)gw).SOD();
				break;
			}
			case "SUBSCRIBE":
			{ // 3) HM
				((ICmmfAdminService)gw).subscribeMarketData();
				break;
			}
			case "LOGOUT": // 4) HO
			{ // optional) Logout
				((ICmmfAdminService)gw).logout();
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
				((ICmmfAdminService)gw).changePassword(password, newPassword);
				break;
			}
			case "GET_INSTRUMENT": // get all instrument from hkex oapi
			{
				((ICmmfAdminService)gw).getAllInstrument();
				break;
			}
			case "QUERY": // 5) HC,password,newPassword
			{ // optional) Logout
				String command = tokens[0];
				String param = "";
				if (tokens.length > 1) {
					param = line.replaceFirst(command, "").substring(1);
				}
				((ICmmfAdminService)gw).query(command, param);
				break;
			}
			case "SET_READY": {
				((ICmmfAdminService)gw).setReady();
				break;
			}
			default:
				logger.error("Admin command not found admin[{}] line[{}]", admin, line);
				break;
			}
		} catch (Exception e)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Admin[" + admin + "] Command[" + line + "]");
			// alert.setContentText(msg);

			alert.showAndWait();

			logger.error("", e);
		}
	}

	public void submit(String line)
	{
		// String line = sc.nextLine();
		String[] tokens = line.split(",");
		switch (tokens[0].toUpperCase())
		{
		case "ON":
		{
			try
			{
				String symbol = tokens[1];
				Double price = Double.parseDouble(tokens[2]);
				Integer qty = Integer.parseInt(tokens[3]);
				String side = tokens[4].toUpperCase();
				Double strike = Double.parseDouble(tokens[5]);
				String giveup = null;
				try
				{
					giveup = tokens[6].toUpperCase();
				} catch (Exception e)
				{
				}
				com.celera.core.dm.ESessionState state = com.celera.core.dm.ESessionState.get(tokens[7]);
				// String month = tokens[7].toUpperCase();
				// Double delta = Double.parseDouble(tokens[8]);

				IInstrument instr = new Derivative("HK", symbol, EInstrumentType.EP, "European Put", null, null, null,
						strike, "", null, false, 0d);
				// IInstrument instr = new Derivative("HK", symbol,
				// EInstrumentType.EP, "European Put",
				// null, null, null, strike, month, null, false,
				// delta);
				// String giveup = "CTOM";
				order = new Order(EOrderStatus.NEW, instr, EOrderType.LIMIT, null, new Date().getTime(), "", price,
						qty, ESide.get(side), giveup, state);
				oms.sendOrder(order);
				Long orderId = order.getOrderId();
				map.put(orderId, order);

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
				// order = map.get(orderId);
				Double price = Double.parseDouble(tokens[2]);
				Integer qty = Integer.parseInt(tokens[3]);
				// order.setOrderId(orderId);
				// order.setPrice(price);
				// order.setQty(qty);
				// order.setStatus(EOrderStatus.AMEND);
				oms.amendOrder(orderId, price, qty, null, null);
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
//				order = map.get(orderId);
//				order.setOrderId(orderId);
//				order.setStatus(EOrderStatus.CANCELLED);
				
				oms.cancelOrder(orderId);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		// TN,symbol,order_id,price,quantity,side,trade report type,my
		// comapny,counter party company
		case "TN": // TN,HHI9800O7,2,439,100,buy,T4,HKTOM (interbank cross T4)
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

				IInstrument instr = new Derivative("HK", symbol, EInstrumentType.EP, "European Put", null, null, null,
						null, "", null, false, 0d);
				// IInstrument instr, EOrderStatus status,
				// ETradeReportType tradeReportType,
				// Integer qty, Double price, Long id, ESide
				// side, String company
				ITradeReport tr = new TradeReport(instr, EOrderStatus.NEW, ETradeReportType.get(trType), ESide.CROSS,
						qty, price, null, refId, myCompany, cpCompany);
				tr.setGroupId(1l);
				
				oms.sendTradeReport(tr);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		case "T2": // T2,2,3,439,100,buy,HSI22000L7,HSI24000L7,HSIJ7 (interbank
					// cross T4)
		{
			int pos = 1;
			try
			{
				Long groupId = Long.parseLong(tokens[pos++]);
				Integer numLegs = Integer.parseInt(tokens[pos++]);
				Double legPrice = Double.parseDouble(tokens[pos++]);
				Integer legQty = Integer.parseInt(tokens[pos++]);
				// ETradeReportType trtype = ETradeReportType.get(sTrType);

				Long refId = new Date().getTime();

				IInstrument instr = new Derivative("HK", "SYHN", EInstrumentType.ECDIAG,
						EInstrumentType.ECDIAG.getName(), null, null, null, null, "", null, false, 0d);
				BlockTradeReport block = new BlockTradeReport(instr, EOrderStatus.PENDING_NEW,
						ETradeReportType.T2_COMBO_CROSS, legQty, legPrice, null, refId, "HKCEL", "HKCEL");
				block.setGroupId(groupId);

				String legSymbol = null;
				Map<Long, java.util.List<ITradeReport>> split = new HashMap<Long, java.util.List<ITradeReport>>();

				for (int i = 0; i < numLegs; i++)
				{
					legSymbol = tokens[pos++];
					groupId = Long.parseLong(tokens[pos++]);
					legPrice = Double.parseDouble(tokens[pos++]);
					legQty = Integer.parseInt(tokens[pos++]);
					EInstrumentType legTrType = EInstrumentType.bySymbol(legSymbol);
					IInstrument legInstr = new Derivative("HK", legSymbol, legTrType, legTrType.getName(), null, null, null, null, "",
							null, false, 0d);
					ITradeReport tr = new TradeReport(legInstr, EOrderStatus.PENDING_NEW, ETradeReportType.T2_COMBO_CROSS,
							ESide.CROSS, legQty, legPrice, null, refId, "HKCEL", "HKCEL");
					tr.setGroupId(groupId);

					ArrayList<ITradeReport> l = (ArrayList<ITradeReport>) split.get(groupId);
					if (l == null)
					{
						l = new ArrayList<ITradeReport>();
						split.put(groupId, l);
					}
					l.add(tr);
//					instr.setSymbol(legSymbol);
				}

				// IOrderGateway gw1 = og.getOrderGateway(legSymbol);
				// gw.createBlockTradeReport(block);
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
		// case "START": // 1) HI,geniumtesting
		// {
		// gw.start();
		// break;
		// }
		// case "LOGIN": // 1) HI,geniumtesting
		// {
		// gw.login(tokens[1]);
		// break;
		// }
		// case "SOD":
		// { // 2) HS
		// gw.SOD();
		// break;
		// }
		// case "SUBSCRIBE":
		// { // 3) HM
		// gw.subscribeMarketData();
		// break;
		// }
		// case "LOGOUT": // 4) HO
		// { // optional) Logout
		// gw.logout();
		// break;
		// }
		// case "CLOSE": // 4) HO
		// { // optional) Logout
		// gw.stop();
		// break;
		// }
		// case "CHANGE_PASSWORD": // 5) HC,password,newPassword
		// { // optional) Logout
		// String password = tokens[1];
		// String newPassword = tokens[2];
		// gw.changePassword(password, newPassword);
		// break;
		// }
		// case "GET_INSTRUMENT": // get all instrument from hkex oapi
		// {
		// gw.getAllInstrument();
		// break;
		// }
		// case "QUERY": // 5) HC,password,newPassword
		// { // optional) Logout
		// String command = tokens[1];
		// gw.query(command);
		// break;
		// }
		default:
		{
			break;
		}
		}
	}

	@Override
	public void onOrder(IOrder o)
	{
		Long ordId = o.getOrderId();
		DisplayOrder old = displayMap.get(ordId);
		if (old == null)
		{
			// if (map.containsKey(ordId)) {

			DisplayOrder display = new DisplayOrder(ordId, o.getInstr().getSymbol(), o.getOrderType().getName(),
					o.getPrice(), o.getQty(), o.getStatus(), o.getRemark());
			displayMap.put(ordId, display);
			data.add(display);
			// data.add(new Person("addFirstName","addLastName","addEmail"));
			// logger.debug("add display person {}", data.size());
		} else
		{
			old.setStatus(o.getStatus());
			old.setRemark(o.getRemark());
			old.setPrice(o.getPrice());
			old.setQty(o.getQty());
            table.refresh();
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
		long ordId = tr.getOrderId();
		DisplayOrder old = displayMap.get(ordId);
		if (old == null)
		{
			DisplayOrder display = new DisplayOrder(ordId, tr.getInstr().getSymbol(), tr.getTradeReportType().getName(),
					tr.getPrice(), tr.getQty(), tr.getStatus(), tr.getRemark());
			displayMap.put(ordId, display);
			data.add(display);
			// data.add(new Person("addFirstName","addLastName","addEmail"));
			// logger.debug("add display person {}", data.size());
		} else
		{
			old.setStatus(tr.getStatus());
			old.setRemark(tr.getRemark());
			old.setPrice(tr.getPrice());
			old.setQty(tr.getQty());
            table.refresh();
		}
	}

	@Override
	public void onInstrumentUpdate(IInstrument i)
	{

	}

	  //Define the button cell
    private class ButtonCell extends TableCell<Record, Boolean> {
        protected final Button button;
        
		ButtonCell(String name)
		{
			 this.button = new Button(name);
		}

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(button);
            }
        }
    }
	
    private class ModifyButtonCell extends ButtonCell {
		ModifyButtonCell() {
			super("Modify");
			// Action when the button is pressed
			button.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent t)
				{
					int i = ModifyButtonCell.this.getIndex();
					// get Selected Item
					DisplayOrder display = (DisplayOrder) ModifyButtonCell.this.getTableView().getItems()
							.get(i);
					Long id = Long.valueOf(display.getOrdId());
					Double price = Double.valueOf(display.getPrice());
					Integer qty = Integer.valueOf(display.getQty());
					oms.amendOrder(id, price, qty, null, null);
				}
			});
		}
    }

    private class CancelButtonCell extends ButtonCell {
    	CancelButtonCell() {
    		super("Cancel");
    		// Action when the button is pressed
    		button.setOnAction(new EventHandler<ActionEvent>()
    		{
    			@Override
    			public void handle(ActionEvent t)
    			{
    				int i = CancelButtonCell.this.getIndex();
    				// get Selected Item
    				DisplayOrder display = (DisplayOrder) CancelButtonCell.this.getTableView().getItems()
    						.get(i);
    				Long id = Long.valueOf(display.getOrdId());
    				oms.cancelOrder(id);
    			}
    		});
    	}
    }

	@Override
	public void onAdmin(byte[] data) {
		ByteBuffer buf = ByteBuffer.allocate(data.length);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(data);
		buf.flip();
		
		char c = (char) buf.get();
		EApp sender = EApp.get(c);
		c = (char) buf.get();
		EMessageType type = EMessageType.get(c);
		c = (char) buf.get();
		EFoCommand command = EFoCommand.get(c);
		
		switch (type) {
		case ADMIN: {
			EAdminAction action = EAdminAction.get((char) buf.get());	
			switch (action) {
				case LOGIN: {
					int pos = buf.position();
					String password = new String(data, pos, 32, java.nio.charset.StandardCharsets.UTF_8);
					((ICmmfAdminService)gw).login(password);
					break;
				}
				case LOGOUT: {
					((ICmmfAdminService)gw).logout();
					break;
				}
				case CHANGE_PASSWORD: {
					int pos = buf.position();
					String password = new String(data, pos, 32, java.nio.charset.StandardCharsets.UTF_8);
					String newPassword = new String(data, pos+32, 32, java.nio.charset.StandardCharsets.UTF_8);
					((ICmmfAdminService)gw).changePassword(password, newPassword);

					break;
				}
				case SOD: {
					break;
				}
				case SUBSCRIBE_MARKET_DATA: {
					break;
				}
				case UNSUBSCRIBE_MARKET_DATA: {
					break;
				}
			}
			break;
		}
		case QUERY: {
			break;
		}
		case RESPONSE: {
			break;
		}
		case TASK: {
			break;
		}
		}
	}
	
	@Override
	public byte[] onQuery(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onResponse(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTask(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSink(byte[] data)
	{
		// TODO Auto-generated method stub
		
	}
    
	// public static class Person {
	//
	// private final SimpleStringProperty firstName;
	// private final SimpleStringProperty lastName;
	// private final SimpleStringProperty email;
	//
	// private Person(String fName, String lName, String email) {
	// this.firstName = new SimpleStringProperty(fName);
	// this.lastName = new SimpleStringProperty(lName);
	// this.email = new SimpleStringProperty(email);
	// }
	//
	// public String getFirstName() {
	// return firstName.get();
	// }
	//
	// public void setFirstName(String fName) {
	// firstName.set(fName);
	// }
	//
	// public String getLastName() {
	// return lastName.get();
	// }
	//
	// public void setLastName(String fName) {
	// lastName.set(fName);
	// }
	//
	// public String getEmail() {
	// return email.get();
	// }
	//
	// public void setEmail(String fName) {
	// email.set(fName);
	// }
	// }
}