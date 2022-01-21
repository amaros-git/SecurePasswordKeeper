import lv.maros.secured.password.keeper.models.PasswordInputData

class TestPasswordInputDataProvider {
    companion object {
        const val INPUT_DATA_ALL_GOOD = 0
        const val INPUT_DATA_PASSWORDS_DO_NOT_MATCH = 1

        fun provide (dataType: Int) =
            when (dataType) {
                INPUT_DATA_PASSWORDS_DO_NOT_MATCH -> {
                    PasswordInputData("website", "username", "12:$#@", "12:$#")
                }
                INPUT_DATA_ALL_GOOD -> {
                    PasswordInputData("www.amazon.com", "user2022", "12:$#wq211);@", "12:$#wq211);@")
                }
                else -> {
                    PasswordInputData("website", "username", "password", "repeatedPassword")
                }
            }
        }
}

class TestPasswordProvider {
    companion object PasswordInputData {
        const val INPUT_DATA_ALL_GOOD = 0
        const val INPUT_DATA_PASSWORD_DO_NOT_MATCH = 0

        fun provide (dataType: Int) {

        }


    }
}