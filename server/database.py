import pymysql


def get_conn():
    conn = pymysql.connect(host='localhost', port=3306, user='root', passwd='', db='societies')
    return conn


def check_session(member_id, session_id):
    conn = get_conn()
    cur = conn.cursor()
    sql = "SELECT * FROM session WHERE(member_id = " + member_id + " AND session_id = '" + session_id + "');"
    cur.execute(sql)
    if cur.rowcount >= 1:
        return True
    else:
        return False
