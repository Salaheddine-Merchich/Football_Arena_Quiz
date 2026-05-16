package com.example.quizapp_merchich;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestionsByContinent(String continent) {
        List<Question> questions = new ArrayList<>();

        switch (continent) {
            case "Africa":
                questions.addAll(getAfricaQuestions());
                break;
            case "Europe":
                questions.addAll(getEuropeQuestions());
                break;
            case "South America":
                questions.addAll(getSouthAmericaQuestions());
                break;
            case "Asia":
                questions.addAll(getAsiaQuestions());
                break;
            default:
                questions.addAll(getAfricaQuestions()); // Default fallback
                break;
        }

        Collections.shuffle(questions);
        if (questions.size() > 20) {
            return new ArrayList<>(questions.subList(0, 20));
        }
        return questions;
    }

    private static List<Question> getAfricaQuestions() {
        List<Question> list = new ArrayList<>();
        // Utilisation de quiz_banner comme illustration par défaut
        list.add(new Question("Which country reached the semi-finals of the 2022 World Cup?", "Morocco", "Senegal", "Cameroon", "Ghana", "Morocco", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which Moroccan club is nicknamed 'The Green Eagles'?", "Raja Casablanca", "Wydad AC", "RS Berkane", "FAR Rabat", "Raja Casablanca", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Who is the all-time top scorer for the Morocco national team?", "Ahmed Faras", "Hakim Ziyech", "Salaheddine Bassir", "Youssef En-Nesyri", "Ahmed Faras", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which nation won the 2023 AFCON title?", "Ivory Coast", "Nigeria", "South Africa", "DR Congo", "Ivory Coast", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which club has won the most CAF Champions League titles?", "Al Ahly", "Zamalek", "TP Mazembe", "Wydad AC", "Al Ahly", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Who is the only African player to win the Ballon d'Or?", "George Weah", "Samuel Eto'o", "Didier Drogba", "Mohamed Salah", "George Weah", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which player won the CAF Player of the Year in 2023?", "Victor Osimhen", "Mohamed Salah", "Achraf Hakimi", "Sadio Mane", "Victor Osimhen", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("In which year did Morocco host the AFCON for the first time?", "1988", "1976", "2004", "1994", "1988", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("What is the nickname of the Senegal national team?", "Teranga Lions", "Indomitable Lions", "The Pharaohs", "The Black Stars", "Teranga Lions", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which club did Mohamed Salah play for in Egypt?", "Al Mokawloon", "Al Ahly", "Zamalek", "Ismaily", "Al Mokawloon", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which Moroccan goalkeeper was the hero in the 2022 WC penalty shootout against Spain?", "Yassine Bounou", "Munir Mohamedi", "Ahmed Tagnaouti", "Anas Zniti", "Yassine Bounou", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Who holds the record for most goals in a single AFCON tournament (9 goals)?", "Ndaye Mulamba", "Laurent Pokou", "Samuel Eto'o", "Rashidi Yekini", "Ndaye Mulamba", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which stadium is the home of both Raja and Wydad?", "Stade Mohammed V", "Stade de Marrakech", "Stade Adrar", "Stade Ibn Batouta", "Stade Mohammed V", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Who is the current head coach of Morocco (2024)?", "Walid Regragui", "Herve Renard", "Vahid Halilhodzic", "Badou Zaki", "Walid Regragui", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which team is nicknamed 'The Indomitable Lions'?", "Cameroon", "Senegal", "Nigeria", "Ivory Coast", "Cameroon", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which city is home to Mamelodi Sundowns?", "Pretoria", "Johannesburg", "Cape Town", "Durban", "Pretoria", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("How many AFCON titles has Egypt won?", "7", "5", "3", "4", "7", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which African legend played for Marseille and AC Milan?", "George Weah", "Abedi Pele", "Didier Drogba", "Samuel Eto'o", "Abedi Pele", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which country will host AFCON 2025?", "Morocco", "Algeria", "Nigeria", "Guinea", "Morocco", R.drawable.quiz_banner, "Africa"));
        list.add(new Question("Which Moroccan player joined PSG from Inter Milan?", "Achraf Hakimi", "Hakim Ziyech", "Sofyan Amrabat", "Nayef Aguerd", "Achraf Hakimi", R.drawable.quiz_banner, "Africa"));
        return list;
    }

    private static List<Question> getEuropeQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which club has won the most UEFA Champions League titles?", "Real Madrid", "AC Milan", "Liverpool", "Bayern Munich", "Real Madrid", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Who is the all-time top scorer in the UCL?", "Cristiano Ronaldo", "Lionel Messi", "Robert Lewandowski", "Karim Benzema", "Cristiano Ronaldo", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which nation won the UEFA Euro 2024?", "Spain", "England", "France", "Germany", "Spain", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("In which stadium does FC Barcelona play?", "Camp Nou", "Santiago Bernabéu", "Metropolitano", "Mestalla", "Camp Nou", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which club won the Premier League 4 times in a row (2021-2024)?", "Manchester City", "Arsenal", "Liverpool", "Chelsea", "Manchester City", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Who is the record goalscorer for the German national team?", "Miroslav Klose", "Gerd Muller", "Thomas Muller", "Lukas Podolski", "Miroslav Klose", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which club is nicknamed 'The Old Lady'?", "Juventus", "Inter Milan", "AC Milan", "AS Roma", "Juventus", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Who won the Ballon d'Or in 2022?", "Karim Benzema", "Kevin De Bruyne", "Sadio Mane", "Robert Lewandowski", "Karim Benzema", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which city hosted the 2024 UCL Final?", "London", "Istanbul", "Paris", "Munich", "London", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which league is known as 'La Liga'?", "Spain", "Italy", "Portugal", "France", "Spain", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which legendary manager won 13 PL titles with Man Utd?", "Sir Alex Ferguson", "Arsene Wenger", "Jose Mourinho", "Pep Guardiola", "Sir Alex Ferguson", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Who holds the record for most PL goals in a single 38-game season?", "Erling Haaland", "Mohamed Salah", "Alan Shearer", "Harry Kane", "Erling Haaland", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which country won the Euro 2020 (held in 2021)?", "Italy", "England", "Spain", "Denmark", "Italy", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which club plays at Anfield?", "Liverpool", "Everton", "Chelsea", "Arsenal", "Liverpool", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("What is the nickname of the France national team?", "Les Bleus", "Les Rouges", "The Three Lions", "Die Mannschaft", "Les Bleus", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which player scored 91 goals in a single calendar year (2012)?", "Lionel Messi", "Cristiano Ronaldo", "Robert Lewandowski", "Luis Suarez", "Lionel Messi", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which club did Kylian Mbappe join in 2024?", "Real Madrid", "Arsenal", "Liverpool", "Man City", "Real Madrid", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("In which league does Bayer Leverkusen play?", "Bundesliga", "Ligue 1", "Serie A", "Eredivisie", "Bundesliga", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Who is the all-time top scorer for England?", "Harry Kane", "Wayne Rooney", "Bobby Charlton", "Gary Lineker", "Harry Kane", R.drawable.quiz_banner, "Europe"));
        list.add(new Question("Which club is nicknamed 'The Red Devils'?", "Manchester United", "Liverpool", "Bayern Munich", "AC Milan", "Manchester United", R.drawable.quiz_banner, "Europe"));
        return list;
    }

    private static List<Question> getSouthAmericaQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which nation won the 2022 FIFA World Cup?", "Argentina", "Brazil", "France", "Croatia", "Argentina", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which country has won the most FIFA World Cups?", "Brazil", "Argentina", "Germany", "Italy", "Brazil", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Who won the 2024 Copa América?", "Argentina", "Colombia", "Uruguay", "Brazil", "Argentina", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which club has won the most Copa Libertadores titles?", "Independiente", "Boca Juniors", "Penarol", "River Plate", "Independiente", R.drawable.quiz_banner, "South America"));
        list.add(new Question("In which stadium does Boca Juniors play?", "La Bombonera", "El Monumental", "Maracanã", "Centenario", "La Bombonera", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which Brazilian player was nicknamed 'The Phenomenon'?", "Ronaldo Nazário", "Ronaldinho", "Rivellino", "Romário", "Ronaldo Nazário", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Who is the all-time recognized top scorer for Brazil (FIFA)?", "Neymar Jr", "Pelé", "Ronaldo", "Romário", "Neymar Jr", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which country hosted the first World Cup in 1930?", "Uruguay", "Brazil", "Argentina", "Chile", "Uruguay", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Who is the iconic captain of Argentina in 1986?", "Diego Maradona", "Lionel Messi", "Daniel Passarella", "Mario Kempes", "Diego Maradona", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which club did Pelé play for most of his career?", "Santos", "Flamengo", "Corinthians", "NY Cosmos", "Santos", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which city is home to the Maracanã stadium?", "Rio de Janeiro", "São Paulo", "Brasília", "Buenos Aires", "Rio de Janeiro", R.drawable.quiz_banner, "South America"));
        list.add(new Question("What is the nickname of the Uruguay national team?", "La Celeste", "La Albiceleste", "Los Incas", "La Canarinha", "La Celeste", R.drawable.quiz_banner, "South America"));
        list.add(new Question("How many World Cups did Pelé win as a player?", "3", "2", "4", "1", "3", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which rivalry is known as 'Superclásico'?", "Boca vs River", "Flamengo vs Fluminense", "Racing vs Independiente", "Nacional vs Peñarol", "Boca vs River", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which player scored the 'Hand of God' goal?", "Diego Maradona", "Lionel Messi", "Luis Suárez", "Pelé", "Diego Maradona", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which South American country won the World Cup in 1978 and 1986?", "Argentina", "Brazil", "Uruguay", "Chile", "Argentina", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Who is the all-time top scorer for Uruguay?", "Luis Suárez", "Edinson Cavani", "Diego Forlán", "Enzo Francescoli", "Luis Suárez", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which club did Ronaldinho play for before joining PSG?", "Gremio", "Flamengo", "Santos", "Atletico Mineiro", "Gremio", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Which country won the Copa América 2021?", "Argentina", "Brazil", "Peru", "Chile", "Argentina", R.drawable.quiz_banner, "South America"));
        list.add(new Question("Who was the manager of Brazil during the 2002 World Cup win?", "Luiz Felipe Scolari", "Tite", "Zagallo", "Carlos Alberto Parreira", "Luiz Felipe Scolari", R.drawable.quiz_banner, "South America"));
        return list;
    }

    private static List<Question> getAsiaQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Which country won the 2023 AFC Asian Cup?", "Qatar", "Jordan", "South Korea", "Japan", "Qatar", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Who is the all-time top scorer for South Korea?", "Cha Bum-kun", "Son Heung-min", "Park Ji-sung", "Lee Dong-gook", "Cha Bum-kun", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which Asian nation has qualified for the most World Cups?", "South Korea", "Japan", "Saudi Arabia", "Iran", "South Korea", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("In which European club does Son Heung-min play (2024)?", "Tottenham Hotspur", "Man City", "Bayern Munich", "Real Madrid", "Tottenham Hotspur", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which country won the most AFC Asian Cup titles?", "Japan", "Saudi Arabia", "South Korea", "Iran", "Japan", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which Saudi club signed Cristiano Ronaldo in 2023?", "Al Nassr", "Al Hilal", "Al Ittihad", "Al Ahli", "Al Nassr", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which team won the AFC Champions League 2023/24?", "Al Ain", "Yokohama Marinos", "Al Hilal", "Ulsan HD", "Al Ain", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which country hosted the 2022 FIFA World Cup?", "Qatar", "Japan", "China", "UAE", "Qatar", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which Asian team reached the semi-finals of the 2002 World Cup?", "South Korea", "Japan", "Turkey", "Saudi Arabia", "South Korea", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which Saudi club is coached by Jorge Jesus and won the 2024 league?", "Al Hilal", "Al Nassr", "Al Shabab", "Al Ittihad", "Al Hilal", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("What is the nickname of the Saudi Arabia national team?", "The Green Falcons", "The Samurai Blue", "The Pharaohs", "The Warriors", "The Green Falcons", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Who was the first Asian player to win the PL Golden Boot?", "Son Heung-min", "Kaoru Mitoma", "Park Ji-sung", "Shinji Kagawa", "Son Heung-min", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("In which city is the AFC headquarters located?", "Kuala Lumpur", "Doha", "Tokyo", "Riyadh", "Kuala Lumpur", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which Japanese legend played for AC Milan and CSKA Moscow?", "Keisuke Honda", "Shinji Kagawa", "Hidetoshi Nakata", "Shunsuke Nakamura", "Keisuke Honda", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which Asian player won the UCL with Manchester United?", "Park Ji-sung", "Shinji Kagawa", "Son Heung-min", "Sun Jihai", "Park Ji-sung", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which country co-hosted the 2002 World Cup with Japan?", "South Korea", "China", "Australia", "Qatar", "South Korea", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Who is the top scorer of the 2023 AFC Asian Cup (8 goals)?", "Akram Afif", "Son Heung-min", "Aymen Hussein", "Ueda", "Akram Afif", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("What is the nickname of the Japan national team?", "Samurai Blue", "Blue Tigers", "Warriors", "Dragons", "Samurai Blue", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which club did Al-Hilal sign Neymar from?", "PSG", "Barcelona", "Santos", "Man City", "PSG", R.drawable.quiz_banner, "Asia"));
        list.add(new Question("Which country won the 2011 Asian Cup?", "Japan", "Australia", "Uzbekistan", "South Korea", "Japan", R.drawable.quiz_banner, "Asia"));
        return list;
    }
}
