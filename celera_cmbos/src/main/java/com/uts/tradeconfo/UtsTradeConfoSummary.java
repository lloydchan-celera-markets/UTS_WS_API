package com.uts.tradeconfo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.backoffice.DatabaseAdapter;
import com.celera.core.configure.IResourceProperties;
import com.celera.core.configure.ResourceManager;
import com.celera.mongo.entity.BOData;
import com.celera.mongo.entity.InvoiceRegister;
import com.uts.tools.Uts2Dm;

import au.com.bytecode.opencsv.CSVReader;

public class UtsTradeConfoSummary
{
	private static final Logger logger = LoggerFactory.getLogger(UtsTradeConfoSummary.class);

	private static final String TRADECONFO_FILE = ResourceManager
			.getProperties(IResourceProperties.PROP_CMBOS_UTSTRADECONFO_CSV);

	public static Map<String, InvoiceRegister> map = new ConcurrentHashMap<String, InvoiceRegister>();
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	public static SimpleDateFormat sdf_yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static void load()
	{
		boolean isHeader = true;

		try
		{
			InvoiceRegister temp = new InvoiceRegister(/*"2016-11-15 00:00", "CEL-160001", "", "", "", 0d*/);
			DatabaseAdapter.deleteAll(temp);

			CSVReader reader = new CSVReader(new FileReader(TRADECONFO_FILE));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null)
			{

				if (nextLine.length == 0)
					continue;
				if (isHeader)
				{
					isHeader = false;
					continue;
				}

				String date = nextLine[2];
				String client = nextLine[3];
				String fees = nextLine[9];
				String fc = nextLine[11];

				com.celera.mongo.entity.Account acc = BOData.get(client);
				if (acc == null)
				{
					logger.debug("No such client {}", client);
				} else
				{
					InvoiceRegister ir = new InvoiceRegister();
//							date, "CEL-160001", client, acc.getId(), fc,
//							Uts2Dm.toDouble(fees));
					try {
						Date d = sdf_yyyyMMddHHmm.parse(date);
						ir.setDate(d);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					ir.setInvoice("CEL-160001");
					ir.setCustomer(client);
					ir.setAccountNumber(acc.getId());
					ir.setCurncy(fc);
					ir.setAmount(Uts2Dm.toDouble(fees));
					ir.setKey(ir.toKey());
					
					InvoiceRegister old = map.get(ir.getKey());
					if (old == null)
					{
						map.put(ir.getKey(), ir);
						logger.debug("New invoice register {}", ir);
					} else
					{
						old.setAmount(ir.getAmount() + old.getAmount());
						logger.debug("Update invoice register {}", old);
					}
				}
			}
			for (InvoiceRegister e : map.values())
			{
				DatabaseAdapter.create(e);
			}

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			// } finally {
			// if (br != null) {
			// try {
			// br.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	public static void main(String[] args) throws IOException
	{
		DatabaseAdapter dba = new DatabaseAdapter();
		dba.start();
		dba.loadAll();
		UtsTradeConfoSummary.load();
	}
}
