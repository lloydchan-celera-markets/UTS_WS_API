package com.celera.core.configure;

public interface IResourceProperties
{
	public static final String PROP_UTS_ENTITY_CODE = "uts.entity.code";
	public static final String PROP_UTS_CLIENT_CODE = "uts.client.code";
	public static final String PROP_UTS_PWD = "uts.password";
	public static final String PROP_UTS_SESSION_ID = "uts.session.id";
	public static final String PROP_UTS_CLIENT_VERESION = "uts.client.version";
	public static final String PROP_UTS_WSDL_FILE = "uts.wsdl.file";
	public static final String PROP_UTS_POLL_FREQ = "uts.poll.frequency";

	public static final String PROP_CXF_SPI_PROVIDER = "cxf.spi.provider";
	
	public static final String PROP_UTS_EMAILTC_SUBJECT_PREFIX = "uts.email.tradeconf.subject";
	public static final String PROP_UTS_EMAILTC_SENDER = "uts.email.tradeconf.sender";
	public static final String PROP_UTS_EMAILTC_ATTACHMENT_PREFIX = "uts.email.tradeconf.attach.prefix";
	public static final String PROP_UTS_EMAILTC_ATTACHMENT_EXT = "uts.email.tradeconf.attach.ext";
	
	public static final String PROP_EMAIL_SERVER_PROTO = "mail.server.protocol";
	public static final String PROP_EMAIL_SERVER_IP = "mail.server.ip";
	public static final String PROP_EMAIL_SERVER_PORT = "mail.server.port";
	public static final String PROP_EMAIL_SERVER_USER = "mail.server.user";
	public static final String PROP_EMAIL_SERVER_PWD = "mail.server.password";
	public static final String PROP_EMAIL_POLL_INTERVAL = "mail.poll.interval";
	public static final String PROP_EMAIL_FILTER_STARTDATE = "mail.filter.startdate";
	public static final String PROP_EMAIL_FILTER_ENDDATE = "mail.filter.enddate";

	public static final String PROP_CM_DBA_STARTDATE = "celera.dba.startdate";
	public static final String PROP_CM_DBA_ENDDATE = "celera.dba.enddate";

	public static final String PROP_CMBOS_CHL_PULL_PROT = "cmbos.channel.pull.protocol";
	public static final String PROP_CMBOS_CHL_PULL_IP = "cmbos.channel.pull.ip";
	public static final String PROP_CMBOS_CHL_PULL_PORT = "cmbos.channel.pull.port";
	public static final String PROP_CMBOS_CHL_SINK_PROT = "cmbos.channel.sink.protocol";
	public static final String PROP_CMBOS_CHL_SINK_IP = "cmbos.channel.sink.ip";
	public static final String PROP_CMBOS_CHL_SINK_PORT = "cmbos.channel.sink.port";
	
	public static final String PROP_CMBOS_TRADECONFO_PATH = "cmbos.tradeconfo.path";
	public static final String PROP_CMBOS_INVOICE_NUMBER = "cmbos.tradeconfo.invoice.number";
	public static final String PROP_CMBOS_INVOICE_EXPORT_PATH = "cmbos.invoice.export.path";
	public static final String PROP_CMBOS_TEMPLATE_PATH = "cmbos.template.path";

	public static final String PROP_CMBOS_UTSTRADECONFO_CSV = "cmbos.uts.tradeconfo.csv";
	
	public static final String PROP_EXCHANGE_CONNECTOR_CHL_PULL_PROT = "exchange.connector.channel.pull.protocol";
	public static final String PROP_EXCHANGE_CONNECTOR_CHL_PULL_IP = "exchange.connector.channel.pull.ip";
	public static final String PROP_EXCHANGE_CONNECTOR_CHL_PULL_PORT = "exchange.connector.channel.pull.port";
	public static final String PROP_EXCHANGE_CONNECTOR_CHL_SINK_PROT = "exchange.connector.channel.sink.protocol";
	public static final String PROP_EXCHANGE_CONNECTOR_CHL_SINK_IP = "exchange.connector.channel.sink.ip";
	public static final String PROP_EXCHANGE_CONNECTOR_CHL_SINK_PORT = "exchange.connector.channel.sink.port";
	
	public static final String PROP_WEB_TRADER_CHL_PULL_PROT = "webtrader.channel.pull.protocol";
	public static final String PROP_WEB_TRADER_CHL_PULL_IP = "webtrader.channel.pull.ip";
	public static final String PROP_WEB_TRADER_CHL_PULL_PORT = "webtrader.channel.pull.port";
	public static final String PROP_WEB_TRADER_CHL_SINK_PROT = "webtrader.channel.sink.protocol";
	public static final String PROP_WEB_TRADER_CHL_SINK_IP = "webtrader.channel.sink.ip";
	public static final String PROP_WEB_TRADER_CHL_SINK_PORT = "webtrader.channel.sink.port";
	public static final String PROP_WEB_TRADER_TRADABLE_SYMBOL = "webtrader.tradable.symbol";
	
	public static final String PROP_HKEX_GATEWAY_CHL_PUSH_PROT = "hkexgw.channel.push.protocol";
	public static final String PROP_HKEX_GATEWAY_CHL_PUSH_IP = "hkexgw.channel.push.ip";
	public static final String PROP_HKEX_GATEWAY_CHL_PUSH_PORT = "hkexgw.channel.push.port";
	public static final String PROP_HKEX_GATEWAY_CHL_SINK_PROT = "hkexgw.channel.sink.protocol";
	public static final String PROP_HKEX_GATEWAY_CHL_SINK_IP = "hkexgw.channel.sink.ip";
	public static final String PROP_HKEX_GATEWAY_CHL_SINK_PORT = "hkexgw.channel.sink.port";
	
	public static final String PROP_COMMON_LIMIT_PRICE_PERCENT = "common.limit.price.percent";
	public static final String PROP_COMMON_LIMIT_QTY = "common.limit.qty";

	public static final String PROP_HKEX_GATEWAY_TESTING = "hkexgw.channel.testing";
	
}
