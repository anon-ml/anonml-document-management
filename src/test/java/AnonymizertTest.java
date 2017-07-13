import com.google.common.collect.Lists;
import ml.anon.docmgmt.extraction.Anonymizer;
import ml.anon.model.anonymization.Anonymization;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by mirco on 01.06.17.
 */
public class AnonymizertTest {

    private String text = "Es gibt im Moment in diese Mannschaft, oh, einige Spieler vergessen ihnen Profi was sie sind. " +
            "Ich lese nicht sehr viele Zeitungen, aber ich habe gehört viele Situationen. Erstens: wir haben nicht offensiv gespielt." +
            " Es gibt keine deutsche Mannschaft spielt offensiv und die Name offensiv wie Bayern. Letzte Spiel hatten wir in Platz drei Spitzen:" +
            " Elber, Jancka und dann Zickler. Wir müssen nicht vergessen Zickler. Zickler ist eine Spitzen mehr, Mehmet eh mehr Basler." +
            " Ist klar diese Wörter, ist möglich verstehen, was ich hab gesagt? Danke. Offensiv, offensiv ist wie machen wir in Platz." +
            " Zweitens: ich habe erklärt mit diese zwei Spieler: nach Dortmund brauchen vielleicht Halbzeit Pause." +
            " Ich habe auch andere Mannschaften gesehen in Europa nach diese Mittwoch. Ich habe gesehen auch zwei Tage die Training. " +
            "Ein Trainer ist nicht ein Idiot! Ein Trainer sei sehen was passieren in Platz. " +
            "In diese Spiel es waren zwei, drei diese Spieler waren schwach wie eine Flasche leer!" +
            " Haben Sie gesehen Mittwoch, welche Mannschaft hat gespielt Mittwoch? Hat gespielt Mehmet oder gespielt Basler oder hat gespielt " +
            "Trapattoni? Diese Spieler beklagen mehr als sie spielen! Wissen Sie, warum die Italienmannschaften kaufen nicht diese Spieler? " +
            "Weil wir haben gesehen viele Male solche Spiel! Haben gesagt sind nicht Spieler für die italienisch Meisters! Strunz! Strunz ist zwei " +
            "Jahre hier, hat gespielt 10 Spiele, ist immer verletzt! Was erlauben Strunz? Letzte Jahre Meister Geworden mit Hamann, eh, Nerlinger. " +
            "Diese Spieler waren Spieler! Waren Meister geworden! Ist immer verletzt! Hat gespielt 25 Spiele in diese Mannschaft in diese Verein." +
            " Muß respektieren die andere Kollegen! haben viel nette kollegen! Stellen Sie die Kollegen die Frage! Haben keine Mut an Worten, aber" +
            " ich weiß, was denken über diese Spieler. Mussen zeigen jetzt, ich will, Samstag, diese Spieler müssen zeigen mich, seine Fans," +
            " müssen alleine die Spiel gewinnen. Muß allein die Spiel gewinnen! Ich bin müde jetzt Vater diese Spieler, eh der Verteidiger " +
            "diese Spieler. Ich habe immer die Schuld über diese Spieler. Einer ist Mario, einer andere ist Mehmet! Strunz ich spreche nicht, " +
            "hat gespielt nur 25 Prozent der Spiel. Ich habe fertig! ...wenn es gab Fragen, ich kann Worte wiederholen...";

    private List<Anonymization> anons = Lists.newArrayList(Anonymization.builder().original("Zickler").replacement("H.").build(),
            Anonymization.builder().original("diese Spieler").replacement("d. s.").build());

    private Anonymizer an = new Anonymizer();

    @Test
    public void anonymize() {
        String anonymize = an.anonymize(text, anons);
        int zickler = StringUtils.countMatches(text, "Zickler");
        int zicklerRepl = StringUtils.countMatches(anonymize, "H.");
        assertThat(anonymize, not(containsString("Zickler")));
        assertThat(anonymize, is(containsString("H.")));
        assertThat(zickler, is(zicklerRepl));

        int spieler = StringUtils.countMatches(text, "diese Spieler");
        int spielerRepl = StringUtils.countMatches(anonymize, "d. s.");
        assertThat(anonymize, not(containsString("diese Spieler")));
        assertThat(anonymize, is(containsString("d. s.")));
        assertThat(spieler, is(spielerRepl));


    }


    @Test
    public void anonymizeWithoutInput() {
        assertThat(an.anonymize(text, Collections.emptyList()), is(text));
        assertThat(an.anonymize(text, null), is(text));
    }
}
