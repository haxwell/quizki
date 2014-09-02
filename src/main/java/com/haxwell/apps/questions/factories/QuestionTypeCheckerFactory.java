package com.haxwell.apps.questions.factories;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.MultiQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.PhraseQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SequenceQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SetQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SingleQuestionTypeChecker;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.QuestionType;

public class QuestionTypeCheckerFactory {

	public static AbstractQuestionTypeChecker getChecker(Question q)
	{
		QuestionType qt = q.getQuestionType();
		
		long qtId = (qt != null ? qt.getId() : -1);
		
		if (qtId == TypeConstants.SINGLE)
			return new SingleQuestionTypeChecker(q);
		else if (qtId == TypeConstants.MULTIPLE)
			return new MultiQuestionTypeChecker(q);
		else if (qtId == TypeConstants.PHRASE)
			return new PhraseQuestionTypeChecker(q);
		else if (qtId == TypeConstants.SEQUENCE)
			return new SequenceQuestionTypeChecker(q);
		else if (qtId == TypeConstants.SET)
			return new SetQuestionTypeChecker(q);
		
		return null;
	}
}
