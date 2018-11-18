package fr.insa.soa.ExchangeSemester.Services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.soa.ExchangeSemester.dao.UserRepository;
import fr.insa.soa.ExchangeSemester.entities.User;

@RestController
public class SignUpService {

	@Autowired
	// This means to get the bean called userRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;

	@PutMapping(value="/signup", produces="application/json")
	public String addAcount(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		boolean found = false;
		Iterable<User> listUsers = userRepository.findAll();

		for (User myUser : listUsers) {
			if (myUser.getLogin().equals(user.getLogin())) {
				found = true;
			}
		}

		if (found) {
			return "{\"success\": \"false\"}";
		} else {	
			byte[] byteChaine = user.getPassword().getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(byteChaine);

			String myHash = DatatypeConverter.printHexBinary(hash);

			user.setPassword(myHash);

			userRepository.save(user);
			return "{\"success\": \"true\"}";
		}
	}
	
/*	@DeleteMapping(value="/signup")
	public String deleteAccount(@RequestBody User user) {
		Iterable<User> listUsers = userRepository.findAll();

		for (User myUser : listUsers) {
			if (myUser.getLogin().equals(user.getLogin())) {
				found = true;
			}
		}
	}*/
	
}