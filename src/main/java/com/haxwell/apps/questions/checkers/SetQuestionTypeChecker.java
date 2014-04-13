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
		this.question = q;
	}
		
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		Set<String> selectedChoiceIds = getChoiceIdsFromMapOfQuestionIdChoiceIdToValue(mapOfFieldNamesToValues);
		
		boolean rtn = true;
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);

		for (Choice c : choices)
		{
			long choiceId = c.getId();
			String dynamicData = (String)this.question.getDynamicData("choiceIdsToBeAnswered");
			String fieldNum = StringUtil.getField(2, ";", ";", dynamicData);

			if (selectedChoiceIds.contains(choiceId+","+fieldNum)) {
				String text = StringUtil.getField(Integer.parseInt(fieldNum), "[[", "]]", c.getText());
				rtn &= mapOfFieldNamesToValues.get(this.question.getId() + "," + choiceId + "," + fieldNum).equals(text);
			}
		}
		
		return rtn;
	}
}
