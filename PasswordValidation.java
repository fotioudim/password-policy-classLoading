/* In this example we have the parameters stored as xml-like string and retrieve them accordingly
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
*/

import java.util.Arrays;
import java.util.List;

import java.io.StringReader;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.passay.EnglishCharacterData;
import org.passay.MatchBehavior;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

// function arguments are the password inserted and the password policy parameters stored in an xml-like file
public Boolean PasswordValidation(Sring password, String xmlFile) {
	SAXBuilder saxBuilder = new SAXBuilder();
	Document document = saxBuilder.build(new StringReader(xmlFile));
	Element classElement = document.getRootElement();

	List<Element> ruleList = classElement.getChildren();
	Object [] argu0 = new Object[ruleList.size()]; // save class and value, unknown datatype so we use object array
	Object [] argu1 = new Object[ruleList.size()];
	Object [] argu2 = new Object[ruleList.size()];
	Rule [] rules = new Rule[ruleList.size()];
	for (int temp = 0; temp < ruleList.size(); temp++) {   
	   Element rule = ruleList.get(temp);
	   List<Element> children = rule.getChildren();
	   String rulename=rule.getChild("Name").getText();
	   if (children.size()>=2){  //1 or more arguments
			String arg0 = rule.getChild("arg0").getText();
			String arg0type = rule.getChild("arg0").getAttribute("type").getValue();
			if (arg0type.equals("char []")) {
				argu0[temp] = ((char[])( arg0.toCharArray()));
			} else if (arg0type.equals("int")) {
				argu0[temp] = new Integer(Integer.parseInt(arg0));	
			} else if (arg0type.equals("EnglishCharacterData")) {
				argu0[temp] = (EnglishCharacterData) EnglishCharacterData.valueOf(arg0);
			}
			if (children.size() >= 3){	//2 or more arguments
				String arg1 = rule.getChild("arg1").getText();
				String arg1type = rule.getChild("arg1").getAttribute("type").getValue();
				if (arg1type.equals("boolean")) {
					argu1[temp] = (boolean) Boolean.parseBoolean(arg1);
				} else if (arg1type.equals("int")) {
					argu1[temp] = new Integer(Integer.parseInt(arg1));	
				} else if (arg1type.equals("MatchBehavior")) {
					argu1[temp] = (MatchBehavior) MatchBehavior.valueOf(arg1);
				}
				if (children.size() == 4) { // WhitespaceRule could have 3 arguments
					String arg2 = rule.getChild("arg2").getText();
					String arg2type = rule.getChild("arg2").getAttribute("type").getValue();
					if (arg2type.equals("boolean")) 
						argu2[temp] = (boolean)Boolean.parseBoolean(arg2);
					// can't get boolean and int primitive type from object array
					rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(argu0[temp].getClass(),argu1[temp].getClass(),boolean.class).newInstance(argu0[temp], argu1[temp], argu2[temp]);
					continue;
				} //use primitive classes in every combination that is needed for the rules used
				if (arg1type.equals("boolean")) // WS Rule
					rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(argu0[temp].getClass(), boolean.class).newInstance(argu0[temp], argu1[temp]);
				else if ((arg0type.equals("int")) && (arg1type.equals("int")))//Length Rule
					rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(int.class, int.class).newInstance(argu0[temp], argu1[temp]);
				else if(arg1type.equals("int"))  // Character Rule
					rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(argu0[temp].getClass().getInterfaces()[0], int.class).newInstance(argu0[temp], argu1[temp]);
				else 
					rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(argu0[temp].getClass(), argu1[temp].getClass()).newInstance(argu0[temp], argu1[temp]);
				continue;
			}
			if (arg0type.equals("int")) // Length Rule
				rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(int.class).newInstance(argu0[temp]);
			else if (arg0type.equals("EnglishCharacterData"))//Character Rule
				rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(argu0[temp].getClass()).newInstance(argu0[temp]);
			else // WSrule
				rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor(argu0[temp].getClass()).newInstance(argu0[temp]);
			continue;
		}
		rules[temp] = (Rule) Class.forName("org.passay."+rulename).getConstructor().newInstance();
	}
	PasswordValidator validator = new PasswordValidator(rules);
	RuleResult result = validator.validate(new PasswordData(password));
	
	return result.isValid();
}
