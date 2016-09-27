/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vectails.api.client;

import com.vectails.message.MessageBuilder;
import com.vectails.message.UtsDirectAccessMessage;
import com.vectalis.B2TDataModel;
import com.vectalis.B2TDataModelSoap;

public final class DirectAccessClient
{
	private DirectAccessClient()
	{
	}

	public static void main(String args[]) throws Exception
	{
		B2TDataModel utsService = new B2TDataModel();
		B2TDataModelSoap port = utsService.getB2TDataModelSoap();

		// login
		String resp = port.updateDirectAccess(MessageBuilder.buildLogin());
		// System.out.println(resp);
		UtsDirectAccessMessage.parseXml(resp);

		resp = port.getAllMyEntityQuotesDirectAccess(MessageBuilder.buildGetAllQuotes());
		// System.out.println(resp);
		UtsDirectAccessMessage.parseXml(resp);

		resp = port.getAllMyEntityQuotesDirectAccessDelta(MessageBuilder.buildGetAllQuotesDelta());
		// System.out.println(resp);
		UtsDirectAccessMessage.parseXml(resp);

		resp = port.getAllMyRepliesDirectAccess(MessageBuilder.buildGetAllMyReplies());
		// System.out.println(resp);
		UtsDirectAccessMessage.parseXml(resp);

		// port.getLoginGenericURL(new String());
		resp = port.ping();
		// System.out.println(resp);

		// logout
		resp = port.updateDirectAccess(MessageBuilder.buildLogout());
		// System.out.println(resp);
		UtsDirectAccessMessage.parseXml(resp);

		System.exit(0);
	}

}
