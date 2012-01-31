package sauer.listentospell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;

public class EnforceSingleWordTextWatcher implements TextWatcher {

  /**
   * Pattern to match any disallowed characters. Allowed are word
   * characters, hyphens ({@literal -}) and apostrophes ({@literal '}).
   */
  private Pattern pattern = Pattern.compile(".*([^-'\\w]+).*");

  public EnforceSingleWordTextWatcher() {
  }

  @Override
  public void afterTextChanged(Editable s) {
    String before = s.toString();
    Matcher matcher = pattern.matcher(before);
    if (matcher.matches()) {
      int start = matcher.start(1);
      int end = matcher.end(1);
      s.delete(start, end);
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }
}
