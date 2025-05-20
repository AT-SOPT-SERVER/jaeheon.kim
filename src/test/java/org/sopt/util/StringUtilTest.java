package org.sopt.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.sopt.common.util.StringUtil;

class StringUtilTest {

	@Test
	void 이모지_수정자_테스트() {
		// assertThat(StringUtil.lengthWithEmoji("\uD83D\uDC4D\uD83C\uDFFB")).isEqualTo(1);
	}

	@Test
	void 교차_이모지_테스트() {
		assertThat(
			StringUtil.lengthWithEmoji("\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66")).isEqualTo(
			1);
	}
}