[Home](http://code.google.com/p/imap2exchange/) | [Intro to Exchange Web Services](ExchangeWebServices.md)

# Introduction to Exchange Web Services #

## Configuring Exchange for Web Services ##

MS Exchange 2007 does provide Web Services out of the box, but you have to set it up.

### Modify https://my.exchange.com/ews/Services.wsdl ###

This WSDL file can be accessed either via the URL above directly or by going to the Web Service like this https://my.exchange.com/ews/Exchange.asmx?wsdl which will just redirect you there. This WSDL file DOES NOT contain a 

&lt;wsdl:service /&gt;

 tag. So you have to add it to the end of the WSDL file.

```
...

   <wsdl:service name="ExchangeServices">
     <wsdl:port name="ExchangeServicePort" binding="tns:ExchangeServiceBinding">
       <soap:address location="https://my.exchange.com/EWS/Exchange.asmx"/>
     </wsdl:port>
   </wsdl:service>

</wsdl:definitions>
```

I also added xml:lang="EN" attribute to the root element in the following files to avoid warnings during WSDL-to-Java code generation.
  * https://my.exchange.com/ews/Services.wsdl
  * https://my.exchange.com/ews/messages.xsd
  * https://my.exchange.com/ews/types.xsd

## Using Java to Access Exchange Web Services ##


### wsdl-to-java Code Generation ###

I tried using Axis2 via the Eclipse Plugin, but it creates a monolithic stub that makes Eclipse puke. I also tried Axis, but it failed for some reason that I can't remember now that I am writing this, but I think it had to do with the complexity of Exchanges XML Schema files. Any how I ended up using JAXWS to generate my stubs. I copied the three files above locally and generated off of those, since Exchange is locked down with ssl.

JAXWS will create two packages:
  1. com.microsoft.schemas.exchange.services._2006.messages
  1. com.microsoft.schemas.exchange.services._2006.types

The most important Classes generated are in the com.microsoft.schemas.exchange.services._2006.messages package: ExchangeServices.java and ExchangeServicePortType, since they are used in connecting to the Web Service. Here is my ExchangeServerFactory class. Please note that you have to Authenticate via Basic Auth to the server._

```
public class YaleExchangeServerPortFactory extends ExchangeServerPortFactory {
	
	String uri;
	String adDomain;
	String uid;
	String pwd;
	ExchangeServices service;
	Authenticator basicAuth;
	
	static{
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(new AllTrustingSocketFactory());
	}
	
	/**
	 *  Set up Authentication
	 *
	 */
	protected void setupAuthenticator(){
		if(basicAuth == null){
			basicAuth = new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication(){
					String superUid = YaleExchangeServerPortFactory.this.adDomain
					                + "\\" 
					                + YaleExchangeServerPortFactory.this.uid;
					String superPwd = YaleExchangeServerPortFactory.this.pwd;
					return new PasswordAuthentication(superUid
							                 ,superPwd.toCharArray());
				}
			};
			Authenticator.setDefault(basicAuth);
		}
	}

	public ExchangeServicePortType getExchangeServerPort() {
		ExchangeServicePortType port = null;
		try{
			port = getExchangeServices().getExchangeServicePort();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		return port;
	}
	
	protected ExchangeServices getExchangeServices() throws Exception{
		setupAuthenticator();
		if(service == null) service = new ExchangeServices(new URL(uri));
		return service;
	}

	public String getAdDomain() {
		return adDomain;
	}

	public void setAdDomain(String adDomain) {
		this.adDomain = adDomain;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
```
One other quick note: If you are connecting to a Development Server, or any server that does not have a Trusted SSL certificate, you will have to import that Cert into your java cert store.

**Keep reading we are almost there!!!!**

You can see from the Factory Class that our proxy object is ExchangeServicePortType. This class needs modification so that you can use it. See blow the example for the explaination of what I did. This is only a sample of the file:
```
//    @WebMethod(operationName = "FindFolder", action = "http://schemas.microsoft.com/exchange/services/2006/messages/FindFolder")
//    public void findFolder(
//        @WebParam(name = "FindFolder", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/messages", partName = "request")
//        FindFolderType request,
//        @WebParam(name = "ExchangeImpersonation", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, partName = "Impersonation")
//        ExchangeImpersonationType impersonation,
//        @WebParam(name = "SerializedSecurityContext", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, partName = "S2SAuth")
//        SerializedSecurityContextType s2SAuth,
//        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
//        @WebParam(name = "MailboxCulture", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, partName = "MailboxCulture")
//        String mailboxCulture,
//        @WebParam(name = "FindFolderResponse", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/messages", mode = WebParam.Mode.OUT, partName = "FindFolderResult")
//        Holder<FindFolderResponseType> findFolderResult,
//        @WebParam(name = "ServerVersionInfo", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, mode = WebParam.Mode.OUT, partName = "ServerVersion")
//        Holder<ServerVersionInfo> serverVersion);

  @WebMethod(operationName = "FindFolder", action = "http://schemas.microsoft.com/exchange/services/2006/messages/FindFolder")
  public void findFolder(
      @WebParam(name = "FindFolder", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/messages", partName = "request")
      FindFolderType request,
      @WebParam(name = "ExchangeImpersonation", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, partName = "Impersonation")
      ExchangeImpersonationType impersonation,
      @WebParam(name = "FindFolderResponse", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/messages", mode = WebParam.Mode.OUT, partName = "FindFolderResult")
      Holder<FindFolderResponseType> findFolderResult,
      @WebParam(name = "ServerVersionInfo", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, mode = WebParam.Mode.OUT, partName = "ServerVersion")
      Holder<ServerVersionInfo> serverVersion);

  @WebMethod(operationName = "FindFolder", action = "http://schemas.microsoft.com/exchange/services/2006/messages/FindFolder")
  public void findFolder(
      @WebParam(name = "FindFolder", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/messages", partName = "request")
      FindFolderType request,
      @WebParam(name = "FindFolderResponse", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/messages", mode = WebParam.Mode.OUT, partName = "FindFolderResult")
      Holder<FindFolderResponseType> findFolderResult,
      @WebParam(name = "ServerVersionInfo", targetNamespace = "http://schemas.microsoft.com/exchange/services/2006/types", header = true, mode = WebParam.Mode.OUT, partName = "ServerVersion")
      Holder<ServerVersionInfo> serverVersion);
```

The commented method signature is what was created by JAXWS. When you use it, the parameter of SerializedSecurityContext does not support isNil, and the call fails if you leave this information out. It is used for server to server communcation. Good luck to ya, if you want to figure it out and use it. By removing the paramter, the element is never written in the soap message getting around the problem. I broke each stub into two signatures to allow for the use of Impersonation.

### MS Exchange Impersonation ###

Impersonation is the use of credentials that have impersonation privileges to perform actions as an other individual. You have to set this up.

Please follow directions here: http://msdn2.microsoft.com/en-us/library/bb204095.aspx

### Exchange Web Service Calls ###

Well... that just about does it for setup, now you can start to write Web Services calls for Exchange. Just to help out, here are two methods I use to get data from Exchange WS.

These are used to get the list of root folders... In this case I only want mail folders, so the results are limited.
```
        public static String EXCHANGE_MAIL_FOLDER_CLASS = "IPF.Note";

	public static List<BaseFolderType> getRootMailFolders(User user){
		DistinguishedFolderIdType root = new DistinguishedFolderIdType();
		root.setId(DistinguishedFolderIdNameType.MSGFOLDERROOT);
		return getChildFolders(user, root);
	}

	public static List<BaseFolderType> getChildFolders(User user, BaseFolderIdType parentFolderId){

		// Create FindFolder
		FindFolderType finder = new FindFolderType();
		
		NonEmptyArrayOfPathsToElementType paths = new NonEmptyArrayOfPathsToElementType();
		paths.getPath().add(PrStatus.PR_STATUS_PATH);

		FolderResponseShapeType folderShape = new FolderResponseShapeType();
		folderShape.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);
		folderShape.setAdditionalProperties(paths);
		
		finder.setFolderShape(folderShape);
		finder.setTraversal(FolderQueryTraversalType.SHALLOW);
		
		IndexedPageViewType index = new IndexedPageViewType();
		index.setBasePoint(IndexBasePointType.BEGINNING);
		index.setOffset(0);
		
		finder.setIndexedPageFolderView(index);
		
		NonEmptyArrayOfBaseFolderIdsType folderIds = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = folderIds.getFolderIdOrDistinguishedFolderId();
		ids.add(parentFolderId);
		finder.setParentFolderIds(folderIds);
		
		// define response Objects and their holders
		FindFolderResponseType findFolderResponse = new FindFolderResponseType();
		Holder<FindFolderResponseType> responseHolder = new Holder<FindFolderResponseType>(findFolderResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		ExchangeServicePortType proxy = null;
		List<BaseFolderType> mailFolders = new ArrayList<BaseFolderType>();
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			proxy.findFolder(finder, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
			                                    .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			                                

			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Get Child Folders Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Get Child Folders Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					FindFolderResponseMessageType findResponse = (FindFolderResponseMessageType)response;
					List<BaseFolderType> allFolders =  findResponse.getRootFolder().getFolders().getFolderOrCalendarFolderOrContactsFolder();
					for(BaseFolderType folder : allFolders){
						mailFolders.add(folder);
					}
				}
			}

		} catch (Exception e){
			throw new RuntimeException("Exception Performing getChildFolders", e);
		} 
		return mailFolders;
		
	}
```

This method is used to return message meta data from within a folder.
```
	public static List<MessageType> getMessages(User user, BaseFolderIdType folderId){
		
		FindItemType finder = new FindItemType();
	
		NonEmptyArrayOfPathsToElementType paths = new NonEmptyArrayOfPathsToElementType();
		paths.getPath().add(CONVERSION_UID_PATH);
		paths.getPath().add(IS_READ_PATH);
		paths.getPath().add(PrMessageFlags.PR_MESSAGE_FLAGS_PATH);

	        ItemResponseShapeType itemShape = new ItemResponseShapeType();		
		itemShape.setBaseShape(DefaultShapeNamesType.ID_ONLY);
		itemShape.setAdditionalProperties(paths);
		finder.setItemShape(itemShape);
	
		finder.setTraversal(ItemQueryTraversalType.SHALLOW);
		
		IndexedPageViewType index = new IndexedPageViewType();
		index.setBasePoint(IndexBasePointType.BEGINNING);
		index.setOffset(0);
		
		finder.setIndexedPageItemView(index);
		
		NonEmptyArrayOfBaseFolderIdsType folderIds = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = folderIds.getFolderIdOrDistinguishedFolderId();
		ids.add(folderId);
		finder.setParentFolderIds(folderIds);
		
		// define response Objects and their holders
		FindItemResponseType findItemResponse = new FindItemResponseType();
		Holder<FindItemResponseType> responseHolder = new Holder<FindItemResponseType>(findItemResponse);
	
		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);
	
		ExchangeServicePortType proxy = null;
		List<MessageType> messages = new ArrayList<MessageType>();
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			proxy.findItem(finder, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
	                   .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Get Messages Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Get Messages Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					FindItemResponseMessageType findResponse = (FindItemResponseMessageType)response;
					for(ItemType item : findResponse.getRootFolder().getItems().getItemOrMessageOrCalendarItem()){
						// This filters out all the messages not moved by an earlier conversion...
						if((item.getExtendedProperty() != null) 
						&& !(item.getExtendedProperty().isEmpty())
						&& item instanceof MessageType){
							messages.add((MessageType)item);
						}
					}
				}
			}
	
		} catch (Exception e){
			throw new RuntimeException("Exception performing getMessages", e);
		} 
		return messages;
	}
```

The last tid-bit of information is that MS actual does a good job on documentation: http://msdn2.microsoft.com/en-us/library/bb204119.aspx

Well that is it for this introduction to Exchange Web Services. I hope I didn't forget anything, and I hope this helps...