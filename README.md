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

LengthRule: https://www.passay.org/javadocs/org/passay/LengthRule.html 

CharacterRule:  http://www.passay.org/javadocs/org/passay/CharacterRule.html

WhiteSpaceRule: http://www.passay.org/javadocs/org/passay/WhitespaceRule.html


