package com.celera.core.dm;

public enum EInstrumentType 
{
	UNKNOWN("Unknown"),
	
	// underlyings
	STOCK("Stock"),
	INDEX("Index"),
	
	// derivatives
	CDS("Credit Derivative Swap"),
	COMBO("Combo"),
	EFP("Exchange For Physical"),
	EQSTR("Total Return Swap"),
	ETF("Exchange Traded Fund"),
	FWD("Forward"),
	FWD_PERCENT("Forward (OTC-Like)"),
	PRS("Price Return Swap"),
	SPRD("Spread"),
	SPRD_PERCENT("Future Spread (OTC-Like)"),
	SPROTC("Future Spread OTC vs Listed"),
	TRSB("Bullet Total Return Swap"),
	ABOX("American Box"),
	ABOX_PERCENT("American Box (OTC-like)"),
	ABOXSWP("American Box Swap"),
	ABOXSWP_PERCENT("American Box Swap (OTC-like)"),
	AC("American Call"),
	AC_PERCENT("American Call (OTC-like)"),
	ACB("American Call Butterfly"),
	ACB_PERCENT("American Call Butterfly (OTC-like)"),
	ACC("American Call Condor"),
	ACC_PERCENT("American Call Condor (OTC-like)"),
	ACCU("Accumulator"),
	ACDIAG("American Call Diagonal"),
	ACDIAG_PERCENT("American Call Diagonal (OTC-like)"),
	ACL("American Call Ladder"),
	ACL_PERCENT("American Call Ladder (OTC-like)"),
	ACR("American Call Ratio"),
	ACR_PERCENT("American Call Ratio (OTC-like)"),
	ACS("American Call Spread"),
	ACS_PERCENT("American Call Spread (OTC-like)"),
	ACSAC("American Call Spread VS Call"),
	ACSAC_PERCENT("American Call Spread VS Call (OTC-like)"),
	ACSAP("American Call Spread Against Put"),
	ACSAP_PERCENT("American Call Spread Against Put (OTC-like)"),
	ACSAPR("American Call Spread VS Put (Ratio)"),
	ACSAPR_PERCENT("American Call Spread VS Put (Ratio) (OTC-like)"),
	ACSAPPO("American Call Spread VS Put - Put Over "),
	ACSAPPO_PERCENT("American Call Spread VS Put - Put Over (OTC-like)"),
	ACSPS("American Call Spread VS Put Spread"),
	ACSPS_PERCENT("American Call Spread VS Put Spread (OTC-like)"),
	ACSTR("American Call Spread Time Ratio"),
	ACSTR_PERCENT("American Call Spread Time Ratio (OTC-like)"),
	ACSTS("American Call Spread Time Spread"),
	ACSTS_PERCENT("American Call Spread Time Spread (OTC-like)"),
	ACSTUP("American Call Stupid"),
	ACSTUP_PERCENT("American Call Stupid (OTC-Like)"),
	ACTB("American Call Time Butterfly"),
	ACTB_PERCENT("American Call Time Butterfly (OTC-like)"),
	ACTC("American Call Time Condor"),
	ACTC_PERCENT("American Call Time Condor (OTC-like)"),
	ACTL("American Call Time Ladder"),
	ACTL_PERCENT("American Call Time Ladder (OTC-like)"),
	ACTR("American Call Time Ratio"),
	ACTR_PERCENT("American Call Time Ratio (OTC-like)"),
	ACTS("American Call Time Spread"),
	ACTS_PERCENT("American Call Time Spread (OTC-like)"),
	ACTSAP("American Call Time Spread Against Put"),
	ACTSAP_PERCENT("American Call Time Spread Against Put (OTC-like)"),
	AECS("Transatlantic Call Spread"),
	AECS_PERCENT("Transatlantic Call Spread (OTC-like)"),
	AECTS("Transatlantic Call Time Spread"),
	AECTS_PERCENT("Transatlantic Call Time Spread (OTC-like)"),
	AEPS("Transatlantic Put Spread"),
	AEPS_PERCENT("Transatlantic Put Spread (OTC-like)"),
	AEPTS("Transatlantic Put Time Spread"),
	AEPTS_PERCENT("Transatlantic Put Time Spread (OTC-like)"),
	AIC("American IronCondor"),
	AIC_PERCENT("American IronCondor (OTC-like)"),
	AIF("American Iron Fly"),
	AIF_PERCENT("American Iron Fly (OTC-like)"),
	AIFR("American Iron Fly Ratio"),
	AIFR_PERCENT("American Iron Fly Ratio (OTC-like)"),
	AJR("American Jelly Roll"),
	AJR_PERCENT("American Jelly Roll (OTC-like)"),
	AJR2S("American Jelly Roll 2-strikes"),
	AJR2S_PERCENT("American Jelly Roll 2-strikes (OTC-like)"),
	AJRPO("American Short Jelly Roll"),
	AJRPO_PERCENT("American Short Jelly Roll (OTC-like)"),
	AJRS2S("American Short Jelly Roll 2-strikes"),
	AJRS2S_PERCENT("American Short Jelly Roll 2-strikes (OTC-like)"),
	ALGUTS("American Long Guts"),
	ALGUTS_PERCENT("American Long Guts (OTC-Like)"),
	AP("American Put"),
	AP_PERCENT("American Put (OTC-like)"),
	APB("American Put Butterfly"),
	APB_PERCENT("American Put Butterfly (OTC-like)"),
	APC("American Put Condor"),
	APC_PERCENT("American Put Condor (OTC-like)"),
	APDIAG("American Put Diagonal"),
	APDIAG_PERCENT("American Put Diagonal (OTC-like)"),
	APEC("Transatlantic Risk Reversal AP-EC"),
	APEC_PERCENT("Transatlantic Risk Reversal AP-EC (OTC-like)"),
	APECT("Transatlantic Time Risk Reversal AP-EC"),
	APECT_PERCENT("Transatlantic Time Risk Reversal AP-EC (OTC-like)"),
	APL("American Put Ladder"),
	APL_PERCENT("American Put Ladder (OTC-like)"),
	APR("American Put Ratio"),
	APR_PERCENT("American Put Ratio (OTC-like)"),
	APS("American Put Spread"),
	APS_PERCENT("American Put Spread (OTC-like)"),
	APSAC("American Put Spread Against Call"),
	APSAC_PERCENT("American Put Spread Against Call (OTC-like)"),
	APSACR("American Put Spread VS Call (Ratio)"),
	APSACR_PERCENT("American Put Spread VS Call (Ratio) (OTC-like)"),
	APSACCO("American Put Spread VS Call - Call Over"),
	APSACCO_PERCENT("American Put Spread VS Call - Call Over (OTC-like)"),
	APSAP("American Put Spread VS Put"),
	APSAP_PERCENT("American Put Spread VS Put (OTC-like)"),
	APSTR("American Put Spread Time Ratio"),
	APSTR_PERCENT("American Put Spread Time Ratio (OTC-like)"),
	APSTS("American Put Spread Time Spread"),
	APSTS_PERCENT("American Put Spread Time Spread (OTC-like)"),
	APSTUP("American Put Stupid"),
	APSTUP_PERCENT("American Put Stupid (OTC-Like)"),
	APTB("American Put Time Butterfly"),
	APTB_PERCENT("American Put Time Butterfly (OTC-like)"),
	APTC("American Put Time Condor"),
	APTC_PERCENT("American Put Time Condor (OTC-like)"),
	APTL("American Put Time Ladder"),
	APTL_PERCENT("American Put Time Ladder (OTC-like)"),
	APTR("American Put Time Ratio"),
	APTR_PERCENT("American Put Time Ratio (OTC-like)"),
	APTS("American Put Time Spread"),
	APTS_PERCENT("American Put Time Spread (OTC-like)"),
	APTSAC("American Put Time Spread Against Call"),
	APTSAC_PERCENT("American Put Time Spread Against Call (OTC-like)"),
	ARR("American Risk Reversal"),
	ARR_PERCENT("American Risk Reversal (OTC-like)"),
	ARRSW("American Risk Reversal Swap"),
	ARRSW_PERCENT("American Risk Reversal Swap (OTC-Like)"),
	ARRTR("American Risk Reversal Time Ratio"),
	ARRTR_PERCENT("American Risk Reversal Time Ratio (OTC-like)"),
	ARRTS("American Risk Reversal Time Spread"),
	ARRTS_PERCENT("American Risk Reversal Time Spread (OTC-like)"),
	AS("American Synthetic Call Over"),
	AS_PERCENT("American Synthetic Call Over (OTC-like)"),
	ASD("American Straddle"),
	ASD_PERCENT("American Straddle (OTC-like)"),
	ASDAC("American Straddle VS Call"),
	ASDAC_PERCENT("American Straddle VS Call (OTC-like)"),
	ASDAP("American Straddle VS Put"),
	ASDAP_PERCENT("American Straddle VS Put (OTC-like)"),
	ASDTS("American Straddle Time Spread"),
	ASDTS_PERCENT("American Straddle Time Spread (OTC-like)"),
	ASDTS2("American Straddle Time Spread 2 UL"),
	ASDTS2_PERCENT("American Straddle Time Spread  2 UL (OTC-like)"),
	ASG("American Strangle"),
	ASG_PERCENT("American Strangle (OTC-like)"),
	ASGAC("American Strangle VS Call"),
	ASGAC_PERCENT("American Strangle VS Call (OTC-like)"),
	ASGAP("American Strangle VS Put"),
	ASGAP_PERCENT("American Strangle VS Put (OTC-like)"),
	ASGTS("American Strangle Time Spread"),
	ASGTS_PERCENT("American Strangle Time Spread (OTC-like)"),
	ASGUTS("American Short Guts"),
	ASGUTS_PERCENT("American Short Guts (OTC-Like)"),
	ASPO("American Synthetic Put Over"),
	ASPO_PERCENT("American Synthetic Put Over (OTC-like)"),
	ASSWP("American Synthetic Swap"),
	ASSWP_PERCENT("American Synthetic Swap (OTC-Like)"),
	ATRR("American Time Risk Reversal"),
	ATRR_PERCENT("American Time Risk Reversal (OTC-like)"),
	ATS("American Time Synthetic"),
	ATS_PERCENT("American Time Synthetic (OTC-like)"),
	BKTC_PERCENT("Basket Call %"),
	BKTP_PERCENT("Basket Put %"),
	BOC_PERCENT("Best Of Call %"),
	BOP_PERCENT("Best Of Put %"),
	CVC2_PERCENT("Call vs Call 2 UL (OTC-like)"),
	CVC3_PERCENT("Call vs Call 3 UL (OTC-like)"),
	DECU("Decumulator"),
	DIVFUT("Dividend Future"),
	DSW("Dividend Swap"),
	DSWB("Divswap Butterfly"),
	DSWTS("Dividend Swap Time Spread"),
	EBOX("European Box"),
	EBOX_PERCENT("European Box (OTC-like)"),
	EBOXSWP("European Box Swap"),
	EBOXSWP_PERCENT("European Box Swap (OTC-like)"),
	EC("European Call"),
	EC_PERCENT("European Call (OTC-like)"),
	ECB("European Call Butterfly"),
	ECB_PERCENT("European Call Butterfly (OTC-like)"),
	ECC("European Call Condor"),
	ECC_PERCENT("European Call Condor (OTC-like)"),
	ECDIAG("European Call Diagonal"),
	ECDIAG_PERCENT("European Call Diagonal (OTC-like)"),
	ECL("European Call Ladder"),
	ECL_PERCENT("European Call Ladder (OTC-like)"),
	ECQ_PERCENT("European Call Quanto (OTC-Like)"),
	ECR("European Call Ratio"),
	ECR_PERCENT("European Call Ratio (OTC-like)"),
	ECS("European Call Spread"),
	ECS_PERCENT("European Call Spread (OTC-like)"),
	ECSAC("European Call Spread VS Call"),
	ECSAC_PERCENT("European Call Spread VS Call (OTC-like)"),
	ECSAP("European Call Spread Against Put"),
	ECSAP_PERCENT("European Call Spread Against Put (OTC-like)"),
	ECSAPR("European Call Spread VS Put (Ratio)"),
	ECSAPR_PERCENT("European Call Spread VS Put (Ratio) (OTC like)"),
	ECSAPPO("European Call Spread VS Put - Put Over"),
	ECSAPPO_PERCENT("European Call Spread VS Put - Put Over (OTC-like)"),
	ECSPS("European Call Spread VS Put Spread"),
	ECSPS_PERCENT("European Call Spread VS Put Spread (OTC-like)"),
	ECSTR("European Call Spread Time Ratio"),
	ECSTR_PERCENT("European Call Spread Time Ratio (OTC-like)"),
	ECSTS("European Call Spread Time Spread"),
	ECSTS_PERCENT("European Call Spread Time Spread (OTC-like)"),
	ECSTUP("European Call Stupid"),
	ECSTUP_PERCENT("European Call Stupid (OTC_Like)"),
	ECTB("European Call Time Butterfly"),
	ECTB_PERCENT("European Call Time Butterfly (OTC-like)"),
	ECTC("European Call Time Condor"),
	ECTC_PERCENT("European Call Time Condor (OTC-like)"),
	ECTL("European Call Time Ladder"),
	ECTL_PERCENT("European Call Time Ladder (OTC-like)"),
	ECTR("European Call Time Ratio"),
	ECTR_PERCENT("European Call Time Ratio (OTC-like)"),
	ECTS("European Call Time Spread"),
	ECTS_PERCENT("European Call Time Spread (OTC-like)"),
	ECTSAP("European Call Time Spread Against Put"),
	ECTSAP_PERCENT("European Call Time Spread Against Put (OTC-like)"),
	EIC("European IronCondor"),
	EIC_PERCENT("European IronCondor (OTC-like)"),
	EIF("European Iron Fly"),
	EIF_PERCENT("European Iron Fly (OTC-like)"),
	EIFR("European Iron Fly Ratio"),
	EIFR_PERCENT("European Iron Fly Ratio (OTC-like)"),
	EJR("European Jelly Roll"),
	EJR_PERCENT("European Jelly Roll (OTC-like)"),
	EJR2S("European Jelly Roll 2-strikes"),
	EJR2S_PERCENT("European Jelly Roll 2-strikes (OTC-like)"),
	EJRPO("European Short Jelly Roll"),
	EJRPO_PERCENT("European Short Jelly Roll (OTC-like)"),
	EJRS2S("European Short Jelly Roll 2-strikes"),
	EJRS2S_PERCENT("European Short Jelly Roll 2-strikes (OTC-like)"),
	ELGUTS("European Long Guts"),
	ELGUTS_PERCENT("European Long Guts (OTC-Like)"),
	ELNC_PERCENT("ELN Call%"),
	ELNP_PERCENT("ELN Put%"),
	EP("European Put"),
	EP_PERCENT("European Put (OTC-like)"),
	EPAC("Transatlantic Risk Reversal EP-AC"),
	EPAC_PERCENT("Transatlantic Risk Reversal EP-AC (OTC-like)"),
	EPACT("Transatlantic Time Risk Reversal EP-AC"),
	EPACT_PERCENT("Transatlantic Time Risk Reversal EP-AC (OTC-like)"),
	EPB("European Put Butterfly"),
	EPB_PERCENT("European Put Butterfly (OTC-like)"),
	EPC("European Put Condor"),
	EPC_PERCENT("European Put Condor (OTC-like)"),
	EPDIAG("European Put Diagonal"),
	EPDIAG_PERCENT("European Put Diagonal (OTC-like)"),
	EPL("European Put Ladder"),
	EPL_PERCENT("European Put Ladder (OTC-like)"),
	EPQ_PERCENT("European Put Quanto (OTC-Like)"),
	EPR("European Put Ratio"),
	EPR_PERCENT("European Put Ratio (OTC-like)"),
	EPS("European Put Spread"),
	EPS_PERCENT("European Put Spread (OTC-like)"),
	EPSAC("European Put Spread Against Call"),
	EPSAC_PERCENT("European Put Spread Against Call (OTC-like)"),
	EPSACR("European Put Spread VS Call (Ratio)"),
	EPSACR_PERCENT("European Put Spread VS Call (Ratio) (OTC-like)"),
	EPSACCO("European Put Spread VS Call - Call Over"),
	EPSACCO_PERCENT("European Put Spread VS Call - Call Over (OTC-like)"),
	EPSAP("European Put Spread VS Put"),
	EPSAP_PERCENT("European Put Spread VS Put (OTC-like)"),
	EPSTR("European Put Spread Time Ratio"),
	EPSTR_PERCENT("European Put Spread Time Ratio (OTC-like)"),
	EPSTS("European Put Spread Time Spread"),
	EPSTS_PERCENT("European Put Spread Time Spread (OTC-like)"),
	EPSTUP("European Put Stupid"),
	EPSTUP_PERCENT("European Put Stupid (OTC_Like)"),
	EPTB("European Put Time Butterfly"),
	EPTB_PERCENT("European Put Time Butterfly (OTC-like)"),
	EPTC("European Put Time Condor"),
	EPTC_PERCENT("European Put Time Condor (OTC-like)"),
	EPTL("European Put Time Ladder"),
	EPTL_PERCENT("European Put Time Ladder (OTC-like)"),
	EPTR("European Put Time Ratio"),
	EPTR_PERCENT("European Put Time Ratio (OTC-like)"),
	EPTS("European Put Time Spread"),
	EPTS_PERCENT("European Put Time Spread (OTC-like)"),
	EPTSAC("European Put Time Spread Against Call"),
	EPTSAC_PERCENT("European Put Time Spread Against Call (OTC-like)"),
	ERR("European Risk Reversal"),
	ERR_PERCENT("European Risk Reversal (OTC-like)"),
	ERRSW("European Risk Reversal Swap"),
	ERRSW_PERCENT("European Risk Reversal Swap (OTC-Like)"),
	ERRTR("European Risk Reversal Time Ratio"),
	ERRTR_PERCENT("European Risk Reversal Time Ratio (OTC-like)"),
	ERRTS("European Risk Reversal Time Spread"),
	ERRTS_PERCENT("European Risk Reversal Time Spread (OTC-like)"),
	ES("European Synthetic Call Over"),
	ES_PERCENT("European Synthetic Call Over (OTC-like)"),
	ESD("European Straddle"),
	ESD_PERCENT("European Straddle (OTC-like)"),
	ESDAC("European Straddle VS Call"),
	ESDAC_PERCENT("European Straddle VS Call (OTC-like)"),
	ESDAP("European Straddle VS Put"),
	ESDAP_PERCENT("European Straddle VS Put (OTC-like)"),
	ESDTS("European Straddle Time Spread"),
	ESDTS_PERCENT("European Straddle Time Spread (OTC-like)"),
	ESDTS2("European Straddle Time Spread  2 UL"),
	ESDTS2_PERCENT("European Straddle Time Spread  2 UL (OTC-like)"),
	ESG("European Strangle"),
	ESG_PERCENT("European Strangle (OTC-like)"),
	ESGAC("European Strangle VS Call"),
	ESGAC_PERCENT("European Strangle VS Call (OTC-like)"),
	ESGAP("European Strangle VS Put"),
	ESGAP_PERCENT("European Strangle VS Put (OTC-like)"),
	ESGTS("European Strangle Time Spread"),
	ESGTS_PERCENT("European Strangle Time Spread (OTC-like)"),
	ESGUTS("European Short Guts"),
	ESGUTS_PERCENT("European Short Guts (OTC-Like)"),
	ESPO("European Synthetic Put Over"),
	ESPO_PERCENT("European Synthetic Put Over (OTC-like)"),
	ESSWP("European Synthetic Swap"),
	ESSWP_PERCENT("European Synthetic Swap (OTC-Like)"),
	ETRR("European Time Risk Reversal"),
	ETRR_PERCENT("European Time Risk Reversal (OTC-like)"),
	ETS("European Time Synthetic"),
	ETS_PERCENT("European Time Synthetic (OTC-like)"),
	EZCC("European Zero Cost Collar"),
	EZCC_PERCENT("European Zero Cost Collar (OTC-like)"),
	FWDB("Forward Butterfly"),
	FWDB_PERCENT("Forward Butterfly (OTC-like)"),
	OUTPRFC_PERCENT("Out Performs Call"),
	RACCRUAL("Range Accrual"),
	SDIC("Simple Barrier Down In Call"),
	SDIC_PERCENT("Simple Barrier Down In Call (OTC-like)"),
	SDIP("Simple Barrier Down In Put"),
	SDIP_PERCENT("Simple Barrier Down In Put (OTC-like)"),
	SDOC("Simple Barrier Down Out Call"),
	SDOC_PERCENT("Simple Barrier Down Out Call (OTC-like)"),
	SDOP("Simple Barrier Down Out Put"),
	SDOP_PERCENT("Simple Barrier Down Out Put (OTC-like)"),
	SSPDQ_PERCENT("Quanto Synthetic Spread"),
	SUIC("Simple Barrier Up In Call"),
	SUIC_PERCENT("Simple Barrier Up In Call (OTC-like)"),
	SUIP("Simple Barrier Up In Put"),
	SUIP_PERCENT("Simple Barrier Up In Put (OTC-like)"),
	SUOC("Simple Barrier Up Out Call"),
	SUOC_PERCENT("Simple Barrier Up Out Call (OTC-like)"),
	SUOP("Simple Barrier Up Out Put"),
	SUOP_PERCENT("Simple Barrier Up Out Put (OTC-like)"),
	TBOOK("Book of European and American Options"),
	VARSPD2("Variance Swap Spread 2 UL"),
	VARSWP("Variance Swap"),
	VARSWPC("Variance Swap Capped"),
	VARSWPTS("Variance Swap Time Spread"),
	VARVOL2("Varswap against Volswap 2 UL"),
	VARVOLS("Varswap against Volswap"),
	VOLAC("Volatility Swap against American Call"),
	VOLAC_PERCENT("Volatility Swap against American Call (OTC-like)"),
	VOLAP("Volatility Swap against American Put"),
	VOLAP_PERCENT("Volatility Swap against American Put (OTC-like)"),
	VOLASD("Volatility Swap against American Straddle"),
	VOLASD_PERCENT("Volatility Swap against American Straddle (OTC-like)"),
	VOLASG("Volatility Swap against American Strangle"),
	VOLASG_PERCENT("Volatility Swap against American Strangle (OTC-like)"),
	VOLEC("Volatility Swap against European Call"),
	VOLEC_PERCENT("Volatility Swap against European Call (OTC-like)"),
	VOLEP("Volatility Swap against European Put"),
	VOLEP_PERCENT("Volatility Swap against European Put (OTC-like)"),
	VOLESD("Volatility Swap against European Straddle"),
	VOLESD_PERCENT("Volatility Swap against European Straddle (OTC-like)"),
	VOLESG("Volatility Swap against European Strangle"),
	VOLESG_PERCENT("Volatility Swap against European Strangle (OTC-like)"),
	VOLSPD2("Volatility Swap Spread 2 UL"),
	VOLSWP("Volatility Swap"),
	VOLSWPC("Volatility Swap Capped"),
	VOLSWSPD("Volatility Swap Spread"),
	VOLVOLC("Volatility Swap against Volatility Swap Capped"),
	VSWAC("Variance Swap against American Call"),
	VSWAC_PERCENT("Variance Swap against American Call (OTC-like)"),
	VSWAP("Variance Swap against American Put"),
	VSWAP_PERCENT("Variance Swap against American Put (OTC-like)"),
	VSWASD("Variance Swap against American Straddle"),
	VSWASD_PERCENT("Variance Swap against American Straddle (OTC-like)"),
	VSWASG("Variance Swap against American Strangle"),
	VSWASG_PERCENT("Variance Swap against American Strangle (OTC-like)"),
	VSWB("Varswap Butterfly"),
	VSWEC("Variance Swap against European Call"),
	VSWEC_PERCENT("Variance Swap against European Call (OTC-like)"),
	VSWEP("Variance Swap against European Put"),
	VSWEP_PERCENT("Variance Swap against European Put (OTC-like)"),
	VSWESD("Variance Swap against European Straddle"),
	VSWESD_PERCENT("Variance Swap against European Straddle (OTC-like)"),
	VSWESG("Variance Swap against European Strangle"),
	VSWESG_PERCENT("Variance Swap against European Strangle (OTC-like)"),
	VSWFS("Forward Start Variance Swap"),
	VSWSW("Variance Swap Switch"),
	VSWVSWC("Variance Swap against Variance Swap Capped"),
	WOC_PERCENT("Worst Of Call %"),
	WOP_PERCENT("Worst Of Put %");

	private final String name;

	EInstrumentType(String name) {
		this.name = name;
	};

	public String getName() {
		return name;
	}
}
