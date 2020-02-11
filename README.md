# password-policy-classLoading
Passay API - password policy enforcement for Java with class loading

Passay uses the two following components:
Rule - one or more rules define a password policy rule set
PasswordValidator - validates a password against a rule set

When implementing password policy in a web app, the kind of rules and their values
may change every time depending on the customer, version etc.

Instead of pinning the values of the rules by hand and determine each time which rules are set, 
we can have the parameters stored in a database and class reflect the passay library every time 
accordingly by implementing them.

Rules used and their constructors dynamically produced based on their documentation
===================================================================================
LengthRule: https://www.passay.org/javadocs/org/passay/LengthRule.html
CharacterRule:  http://www.passay.org/javadocs/org/passay/CharacterRule.html
WhiteSpaceRule: http://www.passay.org/javadocs/org/passay/WhitespaceRule.html

In this example we have the parameters stored as xml-like string and retrieve them accordingly
e.x.
	<PasswordPolicy>
		<Rule>
			<Name>LengthRule</Name>
			<arg0 type="int">8</arg0>
			<arg1 type="int">16</arg1>
		</Rule>
		<Rule>
			<Name>CharacterRule</Name>
			<arg0 type="EnglishCharacterData">UpperCase</arg0>
			<arg1 type="int">5</arg1>
		</Rule>
		<Rule>
			<Name>CharacterRule</Name>
			<arg0 type="EnglishCharacterData">LowerCase</arg0>
			<arg1 type="int">2</arg1>
		</Rule>
		<Rule>
			<Name>CharacterRule</Name>
			<arg0 type="EnglishCharacterData">Digit</arg0>
			<arg1 type="int">1</arg1>
		</Rule>
		<Rule>
			<Name>CharacterRule</Name>
			<arg0 type="EnglishCharacterData">Special</arg0>
			<arg1 type="int">3</arg1>
		</Rule>
     </PasswordPolicy>
