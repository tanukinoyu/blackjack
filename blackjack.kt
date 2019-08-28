fun main(args: Array<String>) {
/**
 * トランプを表現するためにまず空のリストを作成
 * for文を二つ重ねて、マークと数字の種類分繰り返し処理でaddを行っています
 */
    val cards : MutableList<Card> = mutableListOf()
    for(mark in 1..4){
        for(number in 1..13){
            cards.add(Card(mark,number))
        }
    }
    val player = playGame(cards, false)
    val dealer = playGame(cards, true)
    
    judge(player, dealer)
}
/**
 * トランプのカードを表現するためのクラス
 * マークと数字を格納し、printlnでの出力を考慮して各プロパティを
 * String型で新たな変数に代入しています。
 */
data class Card(val mark: Int, var number: Int){
    val markString: String = when(mark){
        1 -> "ダイヤ"
        2 -> "スペード"
        3 -> "ハート"
        4 -> "クローバー"
        else -> "？"
    }
    val numberString: String = when(number){
        1 -> "エース"
        11 -> "ジャック"
        12 -> "クイーン"
        13 -> "キング"
        else -> number.toString()
    }
}
//ゲームをプレイした結果となる手札を表現するクラス
class Hand(val score: Int, val dealer: Boolean, val bj: Boolean, val cards: List<Card>){}

/**
 * プレイヤーとディーラーの手札情報を受け取って出力し、
 * 勝敗を判定する関数です。
 */
fun judge(player: Hand, dealer: Hand){
    println("ゲーム開始です！")
    if(player.score >= 22){
        openHand(player)
        println("プレイヤーがバストしたため、ディーラーの勝ちです！")
    } else if(player.bj){
        openHand(player)
        openHand(dealer)
        if(dealer.bj){
            println("両者ブラックジャック成立です、引き分けでディーラーの勝ちです！")
        } else{
            println("プレイヤーがブラックジャック成立、プレイヤーの勝ちです！")
        }
    } else if(dealer.bj){
        openHand(player)
        openHand(dealer)
        println("ディーラーがブラックジャック成立、ディーラーの勝ちです！")
    } else{
        openHand(player)
        openHand(dealer)
        if(dealer.score >= 22){
            println("ディーラーがバストしたため、プレイヤーの勝ちです！")
        } else if(player.score > dealer.score){
            println("ディーラーのスコアを上回ったので、プレイヤーの勝ちです！")
        } else if(player.score < dealer.score){
            println("ディーラーのスコアを下回ったので、ディーラーの勝ちです！")
        } else{
            println("同スコアにより引き分けなので、ディーラーの勝ちです！！")
        }
    }
}
/**
 * 手札を出力するための関数
 * 受け取ったHandクラスの中のリストに対して、
 * withIndexでインデックスを取得し何枚目のカードかを表し、
 * forEachで各要素に対してprintlnを行っています。
 */
fun openHand(hand: Hand){
    if(hand.dealer){
        hand.cards.withIndex().forEach{println("ディーラーは${it.index + 1}枚目に${it.value.markString}の${it.value.numberString}を引きました")}
        println("ディーラーのスコアは${hand.score}になりました")
        println("----------------------------------------------")
    } else{
        hand.cards.withIndex().forEach{println("プレイヤーは${it.index + 1}枚目に${it.value.markString}の${it.value.numberString}を引きました")}
        println("プレイヤーのスコアは${hand.score}になりました")
        println("----------------------------------------------")
    }
}
/**
 * プレイヤー情報としてディーラーか否かをBooleanで受け取り、
 * カードを引き続けゲームをプレイした結果をクラスに格納して返す関数です。
 */
fun playGame(cards: MutableList<Card>, dealer: Boolean): Hand{
    //引いたカードを手札として格納するために新たに空のリストを作成
    val playCards: MutableList<Card> = mutableListOf()
    //手札の合計点を格納する変数
    var score = 0
    //所持しているエースの枚数を管理するための変数
    var ace = 0
    
    //カードを引き手札に加え、点数を計算する関数
    fun drawCard(){
        //random関数でトランプからランダムでカードを引く、shuffleを使用したほうがトランプっぽかったかも
        val draw = cards.random()
        //引いたカードをリストから消し、手札として作成したリストに格納する
        cards.remove(draw)
        playCards.add(draw)
        /**
         * 引いたカードの数字から、エースや絵札に対応した点数を出す。
         * エースを引いた際にエースの数を表す変数aceの値を増やし、
         * まずは11点のカードとして扱う。
         */
        val drawCardNumber: Int = if(draw.number == 1){
            ace++
            11
        } else if(draw.number >= 11){
            10
        } else{
            draw.number
        }
        score = score + drawCardNumber
        //11点のエースを所持したままバストしそうになった場合に、aceの値を減らしエースを1点として扱う。
        if(ace >= 1 && score >= 22){
            ace--
            score = score - 10
        } 
    }
    /**
     * スコアが一定値を上回るまでカードを引き続ける。
     * ディーラーはルールに則る意味も込めて17点以上になるまでとし、
     * プレイヤーは11点のエースを持っている場合のみ少し攻めてみる事にしました。
     */
    if(dealer){
        while(score < 17){
            drawCard()
        }
    } else{
        while(score < 17){
            drawCard()
        }
        while(ace >= 1 && score <= 18){
            drawCard()
            while(score < 18){
                drawCard()
            }
        }
    }
    //count関数でリストの要素数、つまりは手札の枚数を得る。
    val handCount: Int = playCards.count()
    //ブラックジャックが成立しているかどうかを判定する。
    val blackJack: Boolean = handCount == 2 && score == 21
    //手札クラスに情報を格納し戻り値とする。
    return Hand(score, dealer, blackJack, playCards)
}