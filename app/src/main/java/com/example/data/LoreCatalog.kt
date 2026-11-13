package com.example.data

import com.example.model.CharacterProfile
import com.example.model.CosmicRing
import com.example.model.FactionLoreCard
import com.example.model.HeroStat
import com.example.model.PoliticalCatalystRow
import com.example.model.QuickCodexEntry
import com.example.model.WorldRegion
import com.example.ui.theme.RingFire
import com.example.ui.theme.RingPhantom
import com.example.ui.theme.RingPrana
import com.example.ui.theme.RingVenom
import com.example.ui.theme.RingVoid

object LoreCatalog {
    val heroStats = listOf(
        HeroStat(
            label = "উৎস নথির শব্দ",
            value = "৩৪,১০৪",
            caption = "GNU word count অনুযায়ী",
        ),
        HeroStat(
            label = "মূল লাইন",
            value = "৩,০৭৫",
            caption = "ফাঁকা লাইন-সহ",
        ),
        HeroStat(
            label = "খণ্ড-শিরোনাম ব্লক",
            value = "২০",
            caption = "উৎসে যে ক্রমে আছে",
        ),
        HeroStat(
            label = "মহাজাগতিক বলয়",
            value = "০৫",
            caption = "পঞ্চ-বলয়ের পাঁচ শক্তি",
        ),
    )

    val cosmicRings = listOf(
        CosmicRing(
            indexLabel = "০১",
            name = "অগ্নি",
            powerDescription = "অগ্নির মৌলিক শক্তি — পিঙ্গলা-নাড়ীর উত্তপ্ত লাভা-যাদু ও কালান্তকের ধ্বংসাত্মক তেজ।",
            accentColor = RingFire,
        ),
        CosmicRing(
            indexLabel = "০২",
            name = "তমো",
            powerDescription = "অন্ধকারের শক্তি — মহাশূন্য-খাত ও অমাবস্যার অদৃশ্য মায়াজাল।",
            accentColor = RingVoid,
        ),
        CosmicRing(
            indexLabel = "০৩",
            name = "বিষ",
            powerDescription = "বিষের শক্তি — রসাতলের নাগবংশ ও কালকূটের শীতল হিম-প্রবাহ।",
            accentColor = RingVenom,
        ),
        CosmicRing(
            indexLabel = "০৪",
            name = "নাড়ীপ্রবাহ",
            powerDescription = "নাড়ী ও প্রাণপ্রবাহের শক্তি — ইড়া, পিঙ্গলা ও সুষুম্না চক্রের স্ফটিক-সঞ্চালন।",
            accentColor = RingPrana,
        ),
        CosmicRing(
            indexLabel = "০৫",
            name = "প্রেত-রস",
            powerDescription = "প্রেতলোক-সম্পর্কিত শক্তি — অস্থিপুর ও মাংস-মুদ্রার মেরুদণ্ডীয় অমরত্ব।",
            accentColor = RingPhantom,
        ),
    )

    val worldRegions = listOf(
        WorldRegion(
            id = "kamrup",
            name = "কামরূপ-উত্তরাখণ্ড",
            zoneLabel = "উত্তর হিমালয় খিলান",
            description = "উত্তরের তুষারাবৃত পাহাড়ি অরণ্য; যাযাবর বৃক-মানবের আবাস।",
            dominantForce = "বৃক-উপজাতি মহাজোট (রণজয় ও ব্যাঘ্রদত্ত)",
            mapX = 0.50f,
            mapY = 0.14f,
        ),
        WorldRegion(
            id = "rarh",
            name = "রাঢ় ও দামোদর খাদ",
            zoneLabel = "পশ্চিম অগ্নিগর্ভ",
            description = "পশ্চিমের রুক্ষ লালমাটির মালভূমি; মাটির নিচে সুপ্ত অগ্নিনাথ ড্রাগনের দল।",
            dominantForce = "আর্যবীরের লৌহ-শিবির ও পেটা ইস্পাতের খনি",
            mapX = 0.17f,
            mapY = 0.41f,
        ),
        WorldRegion(
            id = "gaur",
            name = "গৌড়-মহাপীঠ",
            zoneLabel = "কেন্দ্রীয় শ্বেতপাথর দুর্গ",
            description = "মধ্যভূমির শ্বেতপাথরের রাজসিংহাসন ও সাম্রাজ্যিক কেন্দ্র।",
            dominantForce = "গৌড় রাজবংশ, সম্রাট মালাকর ও যুবরাজ রুদ্রপ্রতাপ",
            mapX = 0.51f,
            mapY = 0.36f,
        ),
        WorldRegion(
            id = "tamralipta",
            name = "তাম্রলিপ্ত মহাবন্দর",
            zoneLabel = "দক্ষিণ নৌ-সম্রাজ্য",
            description = "দক্ষিণের স্বর্ণ ও দাসব্যবসার মহানগরী এবং নৌ-বাণিজ্যের কেন্দ্র।",
            dominantForce = "অবন্তিকা মিত্রের মহাব্যাঙ্ক ও সপ্ত-দ্বীপের নৌবহর",
            mapX = 0.35f,
            mapY = 0.72f,
        ),
        WorldRegion(
            id = "sundarbans",
            name = "সুন্দরবন",
            zoneLabel = "দক্ষিণ ম্যানগ্রোভ ব্যূহ",
            description = "দক্ষিণের লোনা ম্যানগ্রোভের অন্তহীন গোলকধাঁধা।",
            dominantForce = "জলদস্যু, গুপ্ত তান্ত্রিক বন্দর ও গোপন জলপথ",
            mapX = 0.67f,
            mapY = 0.61f,
        ),
        WorldRegion(
            id = "mahashunya",
            name = "মহাশূন্য-খাত",
            zoneLabel = "অতল সমুদ্রঘূর্ণি",
            description = "সমুদ্রের অতল গভীরের অনন্ত জলঘূর্ণি; পিশাচকুলের আবাস।",
            dominantForce = "মহা-পিশাচকুল ও কৃষ্ণ-সাগরের অতল স্রোত",
            mapX = 0.64f,
            mapY = 0.83f,
        ),
        WorldRegion(
            id = "rasatal",
            name = "রসাতল",
            zoneLabel = "ভূ-গর্ভস্থ পাতাললোক",
            description = "ভূ-পৃষ্ঠের পাঁচ মাইল নিচের সূর্যহীন পাতাললোক; নাগবংশের আবাস।",
            dominantForce = "নাগকুমারী তক্ষীলা ও প্রাচীন সর্পকুল",
            mapX = 0.84f,
            mapY = 0.44f,
        ),
    )

    val centralCharacters = listOf(
        CharacterProfile(
            id = "aryabeer",
            name = "আর্যবীর রায়বর্মণ",
            titleBadge = "লৌহ-নেকড়ে",
            description = "গৌড়-সিংহাসনের অগ্রজ জারজ সন্তান; রাঢ়ের সীমান্ত সেনাপতি; পিঙ্গলা-নাড়ীর উত্তপ্ত লাভা-যাদু ও বৃক-মানবের আদিম হিংস্রতার অধিকারী।",
            realmTag = "রাঢ় ও দামোদর খাদ",
        ),
        CharacterProfile(
            id = "avantika",
            name = "অবন্তিকা মিত্র",
            titleBadge = "নীল-মণি",
            description = "তাম্রলিপ্তের শীর্ষ নৌ-ব্যাঙ্কিং সিন্ডিকেটের সর্বাধিশ্বরী; ইড়া-নাড়ীর সংবেশন-যাদুর চালক।",
            realmTag = "তাম্রলিপ্ত মহাবন্দর",
        ),
        CharacterProfile(
            id = "rudrapratap",
            name = "যুবরাজ রুদ্রপ্রতাপ সেন",
            titleBadge = "স্বর্ণ-বৃশ্চিক",
            description = "গৌড়ের বৈধ যুবরাজ; পিশাচদের সঙ্গে রক্তচুক্তি করা নিষ্ঠুর ষড়যন্ত্রকারী।",
            realmTag = "গৌড়-মহাপীঠ",
        ),
        CharacterProfile(
            id = "takshila",
            name = "নাগকুমারী তক্ষীলা",
            titleBadge = "রসাতলের রাজকুমারী",
            description = "পাতাল-সাম্রাজ্য রসাতলের শেষ সর্পকুমারী; শীতল রক্ত ও তীব্র বিষের অধিকারী।",
            realmTag = "রসাতল",
        ),
        CharacterProfile(
            id = "ranajoy",
            name = "রণজয় শর্মা",
            titleBadge = "ধূসর-বৃক",
            description = "উত্তরের বৃক-উপজাতির প্রধান; আর্যবীরের রক্তশপথের অনুগত যোদ্ধা।",
            realmTag = "কামরূপ-উত্তরাখণ্ড",
        ),
        CharacterProfile(
            id = "malakar",
            name = "সম্রাট মালাকর ভেন",
            titleBadge = "গৌড়ের সম্রাট",
            description = "গৌড় সাম্রাজ্যের পক্ষাঘাতগ্রস্ত, রুগ্ণ বৃদ্ধ অধিপতি।",
            realmTag = "গৌড়-মহাপীঠ",
        ),
        CharacterProfile(
            id = "theresa",
            name = "গ্র্যান্ড ইনকুইজিটর থেরেসা",
            titleBadge = "পিউরিটি সিনড",
            description = "পিউরিটি সিনডের প্রধান ধর্মীয় মৌলবাদী; সমস্ত রক্ত-যাদু ধ্বংস করতে চায়।",
            realmTag = "ধর্মীয় সংঘ",
        ),
    )

    val extendedPowersVol9 = listOf(
        CharacterProfile(
            id = "indradyumna",
            name = "মহারাজ ইন্দ্রদ্যুম্ন ভৈরব",
            titleBadge = "The Kraken-King of Lanka",
            description = "লঙ্কাদ্বীপের অতল খাতের অধিপতি। তার দেহে মানুষের রক্ত নেই; প্রবাহিত হয় কাঁচা সামুদ্রিক বিষ আর পারদের মিশ্রণ। হাতে রয়েছে বিশালাকার ত্রিধার ব্রোঞ্জ-ত্রিশূল।",
            realmTag = "লঙ্কাদ্বীপ",
            isCentralCast = false,
        ),
        CharacterProfile(
            id = "kalratri",
            name = "মহারানী কালরাত্রি",
            titleBadge = "Witch-Queen of Subarnabhumi",
            description = "সুবর্ণভূমির সোনার দ্বীপগুলোর তান্ত্রিক সম্রাজ্ঞী। তার সারা শরীর অষ্টধাতুর জটিল উল্কিতে ঢাকা। সে সুষুম্না-নাড়ীর মাধ্যমে মৃত যোদ্ধাদের কঙ্কালে জীবন্ত রক্ত সঞ্চার করতে পারে।",
            realmTag = "সুবর্ণভূমি",
            isCentralCast = false,
        ),
        CharacterProfile(
            id = "chitrangad",
            name = "গন্ধর্ব-যোদ্ধা চিত্রাঙ্গদ",
            titleBadge = "The Sky-Reaver",
            description = "মেঘের দেশে বসবাসকারী অর্ধেক মানুষ অর্ধেক পাখি গোত্রের শেষ রাজপুত্র। তার ডানার প্রতিটি পালক রূপার ফলার মতো ধারালো।",
            realmTag = "মেঘলোক",
            isCentralCast = false,
        ),
        CharacterProfile(
            id = "raktanetra",
            name = "অঘোরী তান্ত্রিক রক্তনেত্র",
            titleBadge = "রক্তবীজ স্ফটিক সাধক",
            description = "শ্মশানঘাটের আদি সাধক, যার কপালে তৃতীয় নয়নের জায়গায় জ্বলজ্বল করে একটি জীবন্ত রক্তবীজ স্ফটিক। মানুষের রক্তকে চিরতরে জমাট কাঁচ বানাতে সক্ষম।",
            realmTag = "মহাপীঠ শ্মশান",
            isCentralCast = false,
        ),
    )

    val quickCodexEntries = listOf(
        QuickCodexEntry(
            id = "codex_aryabeer",
            title = "আর্যবীর (লৌহ-নেকড়ে)",
            subtitle = "পিঙ্গলা-নাড়ী ও কালান্তকের প্রভু",
            summary = "গৌড়ের অগ্রজ জারজ সন্তান। পিঙ্গলা-নাড়ীর উত্তপ্ত লাভা-যাদু ও বৃক-মানবের আদিম হিংস্রতার অধিকারী। কালান্তক ড্রাগনের একমাত্র মানবীয় প্রভু।",
        ),
        QuickCodexEntry(
            id = "codex_avantika",
            title = "অবন্তিকা মিত্র (নীল-মণি)",
            subtitle = "তাম্রলিপ্ত মহাশ্রেষ্ঠী ও কাঁচ-জননী",
            summary = "তাম্রলিপ্তের মহাশ্রেষ্ঠী ও সওদাগর। রূপের বাঁধনে পুরুষকে বশ করা বিষকন্যা এবং সংকর ড্রাগন রুদ্রদেবের কাঁচ-জননী।",
        ),
        QuickCodexEntry(
            id = "codex_rudradev",
            title = "রুদ্রদেব ও দ্বৈত ড্রাগন",
            subtitle = "পঞ্চবলয়ের সিলমোহর ও দুই মেরু শক্তি",
            summary = "আর্যবীর ও অবন্তিকার পুত্র রুদ্রদেব—যার কপালে পঞ্চবলয়ের মূল সিলমোহর। তার সাথে কালান্তক (লাভা) ও নীল-নাগ (বরফ) সৃষ্টির দুই মেরু শক্তি।",
        ),
    )

    val factionLoreCards = listOf(
        FactionLoreCard(
            title = "প্রেত-সাম্রাজ্যের অভ্যন্তরীণ রাজনীতি",
            englishTag = "The Pisacha Oligarchy · অস্থিপুর",
            bulletPoints = listOf(
                "মাংস-মুদ্রা (The Marrow Tenders): পিশাচদের প্রধান মুদ্রা সোনা নয়; জীবন্ত উচ্চবর্ণীয় মানবের মেরুদণ্ডের তরল রস। ওখহেভেনের প্রাক্তন রাজারা শত শত বছর এই ঋণে বন্দি ছিল।",
                "লোহিত-চক্ষু পরিষদ (Tri-Cabal): যারা মানুষের কাঁচা রক্ত পান করে তরুণ থাকে এবং গুপ্তচরবৃত্তিতে পারদর্শী।",
                "অস্থি-তক্ষণ গিল্ড (Tri-Cabal): মৃত যোদ্ধাদের কঙ্কাল জোড়া দিয়ে তৈরি করে দুর্ভেদ্য জৈব-বর্ম ও রথ।",
                "বিষ-জিহ্বা ব্যাংক (Tri-Cabal): বিভিন্ন রাজ্যের উত্তরাধিকারীদের ভেতর যুদ্ধ বাঁধিয়ে দিয়ে যুদ্ধের পর ঋণ হিসেবে মানবদেহের হিসেব বুঝে নেয়।",
            ),
        ),
        FactionLoreCard(
            title = "বৃক-উপজাতিসমূহের রক্ত-দ্বন্দ্ব",
            englishTag = "The Werewolf Blood-Pacts · হিমালয় অরণ্য",
            bulletPoints = listOf(
                "ধূসর-বৃক (Grey-Manes): রণজয়ের রক্তধারা। এরা প্রাচীন শপথ ও নৈতিকতায় বিশ্বাসী; মানুষের মাংস খায় না।",
                "রক্ত-দন্ত (Blood-Fangs): বুনো, নরখাদক ও নির্মম। মহাবৃক ব্যাঘ্রদত্তের নেতৃত্বে এরা পূর্ণিমা ছাড়াও যুদ্ধের উন্মাদনায় দানবীয় নেকড়েতে রূপান্তরিত হতে পারে।",
                "হিম-লোম (Frost-Walkers): বরফের নিচে বাস করে; এদের কামড়ে শিকারের রক্ত মুহূর্তে জমে কাঁচ হয়ে যায়।",
            ),
        ),
    )

    val politicalCatalysts = listOf(
        PoliticalCatalystRow(
            name = "আর্যবীর রায়বর্মণ",
            identityAndLineage = "সম্রাট / লৌহ-নেকড়ে",
            politicalGoal = "সমস্ত সামন্তপ্রভুদের দমন করে এক অখণ্ড শাসন প্রতিষ্ঠা করা।",
            hiddenWeakness = "অবন্তিকার প্রতি আদিম অন্ধ আকর্ষণ, যা তাকে প্রায়শই রণকৌশল থেকে বিচ্যুত করে।",
        ),
        PoliticalCatalystRow(
            name = "অবন্তিকা মিত্র",
            identityAndLineage = "সম্রাজ্ঞী / তাম্রলিপ্তের মহাশ্রেষ্ঠী",
            politicalGoal = "তাম্রলিপ্তের নৌবহর ও ধনসম্পদ দিয়ে গোটা উপমহাদেশের অর্থনীতি নিয়ন্ত্রণ করা।",
            hiddenWeakness = "নিজের শরীরের ভেতরে সুপ্ত থাকা স্ফটিক-রক্ত, যা ধীরে ধীরে তার মানবীয় অনুভূতি হরণ করছে।",
        ),
        PoliticalCatalystRow(
            name = "মহাবৃক ব্যাঘ্রদত্ত",
            identityAndLineage = "রক্ত-দন্ত উপজাতির অধিপতি",
            politicalGoal = "আর্যবীরকে হত্যা করে বৃক-জাতিকে সিংহাসনে বসানো।",
            hiddenWeakness = "রণজয়ের প্রতি আদিম গোত্রীয় ঘৃণা; অহংকার।",
        ),
        PoliticalCatalystRow(
            name = "প্রেত-মন্ত্রী কঙ্কালদেব",
            identityAndLineage = "অস্থিপুরের শীর্ষ সওদাগর",
            politicalGoal = "গৌড় ও তাম্রলিপ্তের মাঝে গৃহযুদ্ধ বাঁধিয়ে কোটি কোটি মানুষের রক্তে কোষাগার পূর্ণ করা।",
            hiddenWeakness = "রূপার তৈরি প্রাচীন মন্ত্রপূত অস্ত্র।",
        ),
        PoliticalCatalystRow(
            name = "নাগকুমারী তক্ষীলা",
            identityAndLineage = "রসাতলের দূতী",
            politicalGoal = "পাতালের জলপথের সার্বভৌমিকতা রক্ষা এবং আর্যবীরের বীর্যে নতুন নাগ-বংশধর সৃষ্টি করা।",
            hiddenWeakness = "অবন্তিকার প্রতি তীব্র নারীসুলভ ঈর্ষা।",
        ),
    )

    val geopoliticsAsciiMap = """
                                [ উত্তর: কামরূপ ও তুষার-খিলান ]
                              (হিমগিরির মহাসভা: বৃক-গোষ্ঠীসমূহের মহাজোট)
                                            |
                                            v
[ পশ্চিম: রাঢ়ের অগ্নিগর্ভ ] <----- [ কেন্দ্রীয় গৌড়-মহাপীঠ ] -----> [ পূর্ব: লৌহিত্য ও প্রেত-উপত্যকা ]
(আর্যবীরের লৌহ-শিবির ও         (ভগ্ন সিংহাসন: কুটিল রাজপ্রতিনিধি,        (মহাপিশাচদের স্বাধীন ব্যাঙ্ক,
 পেটা ইস্পাতের খনি)            গুপ্ত ঘাতক ও চক্রান্তকারী পুরোহিতকুল)      কাঁচা অস্থি-মজ্জার বাজার ও দাসব্যবসা)
                                            |
                                            v
                                [ দক্ষিণ: তাম্রলিপ্ত ও লবণাক্ত খাদ ]
                              (অবন্তিকার মহাব্যাঙ্ক ও সপ্ত-দ্বীপের নৌবহর)
    """.trimIndent()

    const val EDITORIAL_POLICY_NOTE =
        "এই ড্যাশবোর্ডে উৎস পাণ্ডুলিপির কোনো পর্ব, অনুচ্ছেদ, সংলাপ, উদ্ধৃতি, চরিত্র-তথ্য বা খণ্ড-শিরোনাম বাদ দেওয়া হয়নি। মূল পাঠের ক্রমও বদলানো হয়নি। বিশ্ব-সূচি ও চরিত্র-কার্ড কেবল দ্রুত নেভিগেশনের জন্য যোগ করা হয়েছে; পূর্ণাঙ্গ পাণ্ডুলিপি পাঠ-বিভাগে সম্পূর্ণভাবে রাখা আছে এবং মূল TXT সংরক্ষণ ও শেয়ারে হুবহু উৎস পাঠ পাওয়া যাবে।"
}
