package com.github.angerona.fw.motivation.utils;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;

public class FolReasonUtils {

	public static final boolean reason(Beliefs b, FolFormula f) {
		return (b.getWorldKnowledge().reason(f).getAnswerValue() == AnswerValue.AV_TRUE);
	}

}
