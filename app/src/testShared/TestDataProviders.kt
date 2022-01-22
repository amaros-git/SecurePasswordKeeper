import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.security.KeeperPasswordManager
import lv.maros.secured.password.keeper.utils.PASSWORD_MIN_LENGTH

class TestPasswordInputDataProvider {
    companion object {
        const val INPUT_DATA_ALL_GOOD = 0
        const val INPUT_DATA_PASSWORDS_DO_NOT_MATCH = 1

        fun provide(dataType: Int): PasswordInputData {
            //If both generated password will be ever same in the future, go and buy lottery ticket :)
            val password1 = KeeperPasswordManager.generatePassword(PASSWORD_MIN_LENGTH)
            val password2 = KeeperPasswordManager.generatePassword(PASSWORD_MIN_LENGTH)
            println("password1 = $password1, password2 = $password2")

            return when (dataType) {
                INPUT_DATA_ALL_GOOD -> {
                    PasswordInputData(
                        "www.amazon.com", "user2022", password1, password1
                    )
                }
                INPUT_DATA_PASSWORDS_DO_NOT_MATCH -> {
                    PasswordInputData(
                        "website", "username", password1, password2
                    )
                }
                else -> {
                    PasswordInputData("website", "username", password1, password1)
                }
            }
        }
    }
}

    class TestPasswordProvider {
        companion object PasswordInputData {
            const val INPUT_DATA_ALL_GOOD = 0
            const val INPUT_DATA_PASSWORD_DO_NOT_MATCH = 0

            fun provide(dataType: Int) {

            }


        }
    }