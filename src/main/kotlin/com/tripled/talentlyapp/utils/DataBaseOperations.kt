package com.tripled.talentlyapp.utils

import org.apache.ibatis.session.SqlSession

fun sessionCloseAndCommit(session: SqlSession) {
    session.commit()
    session.close()
}