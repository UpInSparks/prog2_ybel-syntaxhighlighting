package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {

  // 2.1 collectMatches
  // geht durch jeden Token aus MiniJavaTokens durch den kompletten Text
  // und sucht nach Stellen wo das TokenPattern passt.
  // Für jeden Treffer wird eine HighlightRegion mit Start Ende und Farbe erstellt.
  // Die Liste wird zurück gegeben

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    List<HighlightRegion> result = new ArrayList<>();
    for (Token token : MiniJavaTokens.defaultTokens()) {
      result.addAll(token.test(text)); // test() nutzt matchingGroup korrekt
    }
    return result;
  }

  // 2.2 resolveConflicts
  // resolveConflicts geht durch die sortierte Liste
  // Und entscheidet welche Regionen behaltet werden
  // Jede Region wird überprüft ob sie sich bereits überlappen
  // Überlappen = entfernt, kein Überlappen = Region wird akzeptiert
  // Intervalle sind halb offen = [0, 5) und [5, 8) sind nicht überlappt
  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
    List<HighlightRegion> result = new ArrayList<>();

    for (HighlightRegion candidate : regions) {
      boolean overlaps = false;

      for (HighlightRegion accepted : result) {
        if (candidate.start() < accepted.end() && candidate.end() > accepted.start()) {
          overlaps = true;
          break;
        }
      }
      if (!overlaps) {
        result.add(candidate);
      }
    }
    return result;
  }
}
