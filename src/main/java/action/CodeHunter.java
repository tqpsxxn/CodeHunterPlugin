package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.DocumentUtil;
import dto.CodeHunterRequest;
import dto.CodeHunterResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import utils.HttpUtil;

import java.util.Objects;

public class CodeHunter extends AnAction {

    private final String CODE_HUNTER_URL = "http://127.0.0.1:5000/predict.json";

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 1.获取选中的代码行
        Editor editor =  e.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        Document document = editor.getDocument();
        Integer selectionStart = document.getLineNumber(selectionModel.getSelectionStart());
        Integer selectionEnd = document.getLineNumber(selectionModel.getSelectionEnd());
        String selectedText = selectionModel.getSelectedText();
        System.out.println(selectedText);
        if (StringUtils.isBlank(selectedText)){
            //弹出提示 请求错误
            Messages.showMessageDialog(
                    e.getProject(),
                    "请选择代码",
                    "异常处理插件提示",
                    Messages.getInformationIcon());
            return;
        }

        // 2.调用模型接口获取异常代码行范围以及异常类型
        CodeHunterRequest codeHunterRequest = new CodeHunterRequest(selectedText);
        ResponseEntity<CodeHunterResponse> responseEntity =  HttpUtil.postCodeHunter(CODE_HUNTER_URL, codeHunterRequest);
        if (Objects.isNull(responseEntity) || Objects.isNull(responseEntity.getBody())){
            Messages.showMessageDialog(
                    e.getProject(),
                    "请求错误，请重试",
                    "异常处理插件提示",
                    Messages.getInformationIcon());
        }
        CodeHunterResponse codeHunterResponse  = responseEntity.getBody();
        Integer errorCode = codeHunterResponse.getErrorCode();
        Integer exceptionLinesBegin = codeHunterResponse.getExceptionLinesBegin();
        Integer exceptionLinesEnd = codeHunterResponse.getExceptionLinesEnd();
        String exceptionType = codeHunterResponse.getExceptionType();
        if (errorCode != 0){
            Messages.showMessageDialog(
                    e.getProject(),
                    "服务端发生异常，请联系开发者，错误码：" + errorCode,
                    "异常处理插件提示",
                    Messages.getInformationIcon());
            return;
        }
        if (exceptionLinesBegin <= 0 || exceptionLinesEnd <= 0){
            Messages.showMessageDialog(
                    e.getProject(),
                    "没有异常，无需处理！",
                    "异常处理插件提示",
                    Messages.getInformationIcon());
            return;
        }
        //3. 添加try-catch块
        int tryStartLineNumber = selectionStart + exceptionLinesBegin;
        int tryEndLineNumber = selectionStart + exceptionLinesEnd;
        int lineStartOffset = document.getLineStartOffset(tryStartLineNumber);
        CharSequence indentSeq = DocumentUtil.getIndent(document, lineStartOffset);
        WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
            // try块起始位置
            document.insertString(lineStartOffset, indentSeq.toString() + "try{\n");
            // 每行缩进
            for(int index = tryStartLineNumber + 1; index <= tryEndLineNumber; index++){
                int inLineIndexOffset = document.getLineStartOffset(index);
                document.insertString(inLineIndexOffset, "    ");
            }
            // catch块起始位置
            int lineEndOffset = document.getLineEndOffset(tryEndLineNumber);
            document.insertString(lineEndOffset + indentSeq.length(),
                         "\n" + indentSeq + "}catch("
                            + exceptionType + " e){\n" + indentSeq
                            + "    // todo add exception handling code\n"
                            + indentSeq + "}\n"+ indentSeq);
        });
    }
}
