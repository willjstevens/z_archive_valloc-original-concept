package com.valloc.security;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.valloc.security.auth.AuthServiceTest;
import com.valloc.security.crypto.CryptoServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SecuritySupportServiceTest.class,
	CryptoServiceTest.class,
	AuthServiceTest.class
})
public class SecurityTests {}