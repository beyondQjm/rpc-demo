package qjm.rpc.test;

import qjm.rpc.client.RpcClientProxy;
import qjm.rpc.test.imp.Person;
import qjm.rpc.test.imp.PersonService;

public class ClientTest {
	
	public static void main(String[] args) {
		RpcClientProxy proxy = new RpcClientProxy("127.0.0.1",9998);
		PersonService service = proxy.getProxy(PersonService.class);
		
		System.out.println(service.getInfo());
		
		Person person = new Person();
		person.setAge(23);
		person.setName("Qjm");
		person.setSex("男");
		System.out.println(service.printInfo(person));
	}
}
