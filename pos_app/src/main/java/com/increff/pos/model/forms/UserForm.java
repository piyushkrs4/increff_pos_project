package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {

	@NotBlank
	@Email
	private String email;
	@NotBlank
	@Size(min = 8, max = 12)
	private String password;
	@NotBlank
	private String role;
}
