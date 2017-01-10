package com.celera.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;

import com.celera.core.dm.Derivative;
import com.celera.core.dm.EInstrumentType;
import com.celera.core.dm.EOGAdmin;
import com.celera.core.dm.EOrderStatus;
import com.celera.core.dm.EOrderType;
import com.celera.core.dm.ESide;
import com.celera.core.dm.ETradeReportType;
import com.celera.core.dm.IInstrument;
import com.celera.core.dm.IOrder;
import com.celera.core.dm.Order;
import com.celera.core.dm.TradeReport;
import com.celera.message.cmmf.ECommand;
import com.itextpdf.text.List;

public class HKExCertTest
{
	public static void main(String[] args)
	{
		HkexOapiGateway gw = new HkexOapiGateway();
		gw.init();
		gw.start();

		Map<Long, IOrder> map = new HashMap<Long, IOrder>();
		Map<Long, ArrayList<IOrder>> blocks = new HashMap<Long, ArrayList<IOrder>>();

		IOrder order = null;

		// EOrderStatus status, IInstrument instr, EOrderType type, Long id,
		// String entity,
		// Double price, Integer qty
		// IInstrument instr = new Derivative("HK", "HHI9800O7",
		// EInstrumentType.EP, "European Put", null, null, null,
		// 9800d, "O7", null, false, 0.5);
		// IOrder order = new Order(EOrderStatus.SENT, instr, EOrderType.LIMIT,
		// 1l, "", 439d, 100, ESide.BUY);

		while (true)
		{
			try
			{
				Scanner sc = new Scanner(System.in);
				System.out.println("Program started:");
				while (sc.hasNextLine())
				{
					String line = sc.nextLine();
					String[] tokens = line.split(",");
					switch (tokens[0].toUpperCase())
					{
					case "START": // 1) HI,geniumtesting
					{
						gw.start(tokens[1]);
						break;
					}
					case "LOGIN": // 1) HI,geniumtesting
					{
						gw.login(tokens[1]);
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
					case "CHANGE_PASSWORD": // 5) HC,password,newPassword
					{ // optional) Logout
						String password = tokens[1];
						String newPassword = tokens[2];
						gw.changePassword(password, newPassword);
						break;
					}
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
							// String month = tokens[7].toUpperCase();
							// Double delta = Double.parseDouble(tokens[8]);

							IInstrument instr = new Derivative("HK", symbol, EInstrumentType.EP, "European Put", null,
									null, null, strike, "", null, false, 0d);
							// IInstrument instr = new Derivative("HK", symbol,
							// EInstrumentType.EP, "European Put",
							// null, null, null, strike, month, null, false,
							// delta);
							order = new Order(EOrderStatus.NEW, instr, EOrderType.LIMIT, orderId, null, "", price, qty,
									ESide.get(side));
							gw.createOrder(order);
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
							order = map.get(orderId);
							Double price = Double.parseDouble(tokens[2]);
							Integer qty = Integer.parseInt(tokens[3]);
							order.setId(orderId);
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
							order.setId(orderId);
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
							String symbol = tokens[1];
							Long orderId = Long.parseLong(tokens[2]);
							Double price = Double.parseDouble(tokens[3]);
							Integer qty = Integer.parseInt(tokens[4]);
							String side = tokens[5].toUpperCase();
							String trType = tokens[6].toUpperCase();
							String myCompany = tokens[7];
							String cpCompany = tokens[8];

							IInstrument instr = new Derivative("HK", symbol, EInstrumentType.EP, "European Put", null,
									null, null, null, "", null, false, 0d);
							// IInstrument instr, EOrderStatus status,
							// ETradeReportType tradeReportType,
							// Integer qty, Double price, Long id, ESide
							// side, String company
							order = new TradeReport(instr, EOrderStatus.NEW, ETradeReportType.get(trType), qty, price,
									orderId, null, ESide.get(side), myCompany, cpCompany);
							gw.createTradeReport(order);
							map.put(orderId, order);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						break;
					}
					case "TB":	// TN,HHI9800O7,2,439,100,buy,T4,HKTOM    (interbank cross T4)
					{
						int pos = 1;
						int size = tokens.length;
						try
						{
							ArrayList<IOrder> list = new ArrayList<IOrder>();
							Long orderId = Long.parseLong(tokens[pos++]);
							String synSymbol = tokens[pos++];
							for (int i=1; i<size; i++) 
							{
								String legSymbol = tokens[pos++];
								Double legPrice = Double.parseDouble(tokens[pos++]);
								Integer legQty = Integer.parseInt(tokens[pos++]);
								String legSide = tokens[pos++].toUpperCase();
								String legMyCompany = tokens[pos++];
								String legCpCompany = tokens[pos++];
								
								IInstrument instr = new Derivative("HK", legSymbol, EInstrumentType.EP, "European Put", null,
										null, null, null, "", null, false, 0d);
								order = new TradeReport(instr, EOrderStatus.NEW, ETradeReportType.T2_COMBO_CROSS, legQty, legPrice,
										orderId, null, ESide.get(legSide), legMyCompany, legCpCompany);
								list.add(order);
							}
							gw.createBlockTradeReport(list, orderId, synSymbol);
							blocks.put(orderId, list);
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
							order.setId(orderId);
							gw.cancelTradeReport(order);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						break;
					}
					default:
					{
						break;
					}
					}
					// gw.createOrder(order);
					// Thread.sleep(10000);
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
