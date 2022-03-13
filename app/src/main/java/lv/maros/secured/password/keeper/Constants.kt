package lv.maros.secured.password.keeper

const val PASSWORD_REMOVAL_SNACKBAR_DURATION: Int = 3000 // ms
const val PASSWORD_REMOVAL_WORKER_INITIAL_DELAY: Long =
    PASSWORD_REMOVAL_SNACKBAR_DURATION.toLong() + 1000 //ms

const val PASSWORD_IDS_TO_REMOVE_KEY = "passwordIdsToRemove"
const val PASSWORDS_REMOVAL_TAG = "PASSWORDS_REMOVAL"
const val PASSWORDS_REMOVAL_WORK_NAME = "passwordsRemoval"