package com.haxwell.apps.questions.checkers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class SetQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SetQuestionTypeChecker(Question q) {
		super(q);
	}
		
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		Set<String> selectedChoiceIds = getChoiceIdsFromMapOfQuestionIdChoiceIdToValue(mapOfFieldNamesToValues);
		
		boolean rtn = true;
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		String dynamicData = (String)this.question.getDynamicData("choiceIdsToBeAnswered");
		String fieldNum = StringUtil.getField(2, ";", ";", dynamicData);

		for (Choice c : choices)
		{
			long choiceId = c.getId();

			if (selectedChoiceIds.contains(choiceId+","+fieldNum)) {
				String text = null;
				
				if (StringUtil.equals(fieldNum, "-1")) {
					text = c.getText();
				}
				else {
					text = StringUtil.getField(Integer.parseInt(fieldNum), "[[", "]]", c.getText());
				}

				String str = mapOfFieldNamesToValues.get(this.question.getId() + "," + choiceId + "," + fieldNum); 
				rtn &= str.toLowerCase().equals(text.toLowerCase());
			}
		}
		
		return rtn;
	}
}
