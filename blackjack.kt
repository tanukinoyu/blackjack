fun main(args: Array<String>) {
/**
 * �g�����v��\�����邽�߂ɂ܂���̃��X�g���쐬
 * for�����d�˂āA�}�[�N�Ɛ����̎�ޕ��J��Ԃ�������add���s���Ă��܂�
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
 * �g�����v�̃J�[�h��\�����邽�߂̃N���X
 * �}�[�N�Ɛ������i�[���Aprintln�ł̏o�͂��l�����Ċe�v���p�e�B��
 * String�^�ŐV���ȕϐ��ɑ�����Ă��܂��B
 */
data class Card(val mark: Int, var number: Int){
    val markString: String = when(mark){
        1 -> "�_�C��"
        2 -> "�X�y�[�h"
        3 -> "�n�[�g"
        4 -> "�N���[�o�["
        else -> "�H"
    }
    val numberString: String = when(number){
        1 -> "�G�[�X"
        11 -> "�W���b�N"
        12 -> "�N�C�[��"
        13 -> "�L���O"
        else -> number.toString()
    }
}
//�Q�[�����v���C�������ʂƂȂ��D��\������N���X
class Hand(val score: Int, val dealer: Boolean, val bj: Boolean, val cards: List<Card>){}

/**
 * �v���C���[�ƃf�B�[���[�̎�D�����󂯎���ďo�͂��A
 * ���s�𔻒肷��֐��ł��B
 */
fun judge(player: Hand, dealer: Hand){
    println("�Q�[���J�n�ł��I")
    if(player.score >= 22){
        openHand(player)
        println("�v���C���[���o�X�g�������߁A�f�B�[���[�̏����ł��I")
    } else if(player.bj){
        openHand(player)
        openHand(dealer)
        if(dealer.bj){
            println("���҃u���b�N�W���b�N�����ł��A���������Ńf�B�[���[�̏����ł��I")
        } else{
            println("�v���C���[���u���b�N�W���b�N�����A�v���C���[�̏����ł��I")
        }
    } else if(dealer.bj){
        openHand(player)
        openHand(dealer)
        println("�f�B�[���[���u���b�N�W���b�N�����A�f�B�[���[�̏����ł��I")
    } else{
        openHand(player)
        openHand(dealer)
        if(dealer.score >= 22){
            println("�f�B�[���[���o�X�g�������߁A�v���C���[�̏����ł��I")
        } else if(player.score > dealer.score){
            println("�f�B�[���[�̃X�R�A���������̂ŁA�v���C���[�̏����ł��I")
        } else if(player.score < dealer.score){
            println("�f�B�[���[�̃X�R�A����������̂ŁA�f�B�[���[�̏����ł��I")
        } else{
            println("���X�R�A�ɂ����������Ȃ̂ŁA�f�B�[���[�̏����ł��I�I")
        }
    }
}
/**
 * ��D���o�͂��邽�߂̊֐�
 * �󂯎����Hand�N���X�̒��̃��X�g�ɑ΂��āA
 * withIndex�ŃC���f�b�N�X���擾�������ڂ̃J�[�h����\���A
 * forEach�Ŋe�v�f�ɑ΂���println���s���Ă��܂��B
 */
fun openHand(hand: Hand){
    if(hand.dealer){
        hand.cards.withIndex().forEach{println("�f�B�[���[��${it.index + 1}���ڂ�${it.value.markString}��${it.value.numberString}�������܂���")}
        println("�f�B�[���[�̃X�R�A��${hand.score}�ɂȂ�܂���")
        println("----------------------------------------------")
    } else{
        hand.cards.withIndex().forEach{println("�v���C���[��${it.index + 1}���ڂ�${it.value.markString}��${it.value.numberString}�������܂���")}
        println("�v���C���[�̃X�R�A��${hand.score}�ɂȂ�܂���")
        println("----------------------------------------------")
    }
}
/**
 * �v���C���[���Ƃ��ăf�B�[���[���ۂ���Boolean�Ŏ󂯎��A
 * �J�[�h�����������Q�[�����v���C�������ʂ��N���X�Ɋi�[���ĕԂ��֐��ł��B
 */
fun playGame(cards: MutableList<Card>, dealer: Boolean): Hand{
    //�������J�[�h����D�Ƃ��Ċi�[���邽�߂ɐV���ɋ�̃��X�g���쐬
    val playCards: MutableList<Card> = mutableListOf()
    //��D�̍��v�_���i�[����ϐ�
    var score = 0
    //�������Ă���G�[�X�̖������Ǘ����邽�߂̕ϐ�
    var ace = 0
    
    //�J�[�h��������D�ɉ����A�_�����v�Z����֐�
    fun drawCard(){
        //random�֐��Ńg�����v���烉���_���ŃJ�[�h�������Ashuffle���g�p�����ق����g�����v���ۂ���������
        val draw = cards.random()
        //�������J�[�h�����X�g��������A��D�Ƃ��č쐬�������X�g�Ɋi�[����
        cards.remove(draw)
        playCards.add(draw)
        /**
         * �������J�[�h�̐�������A�G�[�X��G�D�ɑΉ������_�����o���B
         * �G�[�X���������ۂɃG�[�X�̐���\���ϐ�ace�̒l�𑝂₵�A
         * �܂���11�_�̃J�[�h�Ƃ��Ĉ����B
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
        //11�_�̃G�[�X�����������܂܃o�X�g�������ɂȂ����ꍇ�ɁAace�̒l�����炵�G�[�X��1�_�Ƃ��Ĉ����B
        if(ace >= 1 && score >= 22){
            ace--
            score = score - 10
        } 
    }
    /**
     * �X�R�A�����l������܂ŃJ�[�h������������B
     * �f�B�[���[�̓��[���ɑ���Ӗ������߂�17�_�ȏ�ɂȂ�܂łƂ��A
     * �v���C���[��11�_�̃G�[�X�������Ă���ꍇ�̂ݏ����U�߂Ă݂鎖�ɂ��܂����B
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
    //count�֐��Ń��X�g�̗v�f���A�܂�͎�D�̖����𓾂�B
    val handCount: Int = playCards.count()
    //�u���b�N�W���b�N���������Ă��邩�ǂ����𔻒肷��B
    val blackJack: Boolean = handCount == 2 && score == 21
    //��D�N���X�ɏ����i�[���߂�l�Ƃ���B
    return Hand(score, dealer, blackJack, playCards)
}