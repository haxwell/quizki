package com.haxwell.apps.questions.dynamifiers;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.entities.AbstractEntity;

public abstract class AbstractDynamifier {

	public void dynamify(AbstractEntity ae, HttpServletRequest request) { }
}
