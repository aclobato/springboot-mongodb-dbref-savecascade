package com.example.demobd;

import com.example.demobd.model.Contract;
import com.example.demobd.model.User;
import com.example.demobd.repository.ContractRepositoty;
import com.example.demobd.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemobdApplicationTests {

	@Autowired
	private ContractRepositoty contractRepositoty;

	@Autowired
	private UserRepository userRepository;

	@Test
	void createUserBefore() {
		User user = new User();
		user.setName("Usuário 1");
		userRepository.save(user);

		Contract contract = new Contract();
		contract.setName("Contrato 1");

		List<User> users = new ArrayList<>();
		users.add(user);
		contract.setUsers(users);

		contractRepositoty.save(contract);
	}

	@Test
	void createCascading() {
		Contract contract = new Contract();
		contract.setName("Contrato 2");

		User user = new User();
		user.setName("Usuário 2");

		List<User> users = new ArrayList<>();
		users.add(user);
		contract.setUsers(users);

		contractRepositoty.save(contract);
	}

	@Test
	void createAdminCascading() {
		Contract contract = new Contract();
		contract.setName("Contrato 3");

		User admin = new User();
		admin.setName("Admin");
		contract.setAdmin(admin);

		contractRepositoty.save(contract);
	}

	@Test
	void editUserCascading() {
		Contract contract = new Contract();
		contract.setName("Contrato 4");

		User user = new User();
		user.setName("Usuário 4");

		List<User> users = new ArrayList<>();
		users.add(user);
		contract.setUsers(users);

		contract = contractRepositoty.save(contract);

		contract.getUsers().get(0).setName("Editado");
		contractRepositoty.save(contract);
	}

}
