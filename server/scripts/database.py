import pymysql

class Database:
    conn = pymysql.connect(host='localhost', port=3306, user='root', passwd='', db='societies')
    cur = conn.cursor()

    def check_session(self, member_id, session_id):
        sql = "SELECT * FROM session WHERE(member_id = " + member_id + " AND session_id = '" + session_id + "');"
        self.cur.execute(sql)
        if self.cur.rowcount >= 1:
            return True
        else:
            return False
    
    def check_committee(self, member_id, society_id):
        sql = "SELECT * FROM committee_society WHERE(member_id = " + member_id + " AND society_id = " + society_id + ")"
        self.cur.execute(sql)
        if self.cur.rowcount == 1:
            return True
        else:
            return False
    
    def check_chair(self, member_id, society_id):
        sql = "SELECT * FROM society WHERE(chair_id = " + member_id + " AND society_id = " + society_id + ")"
        self.cur.execute(sql)
        if self.cur.rowcount == 1:
            return True
        else:
            return False
    
    def close(self):
        self.conn.commit()
        self.conn.close()

